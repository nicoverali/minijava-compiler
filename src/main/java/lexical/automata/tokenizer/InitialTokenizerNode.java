package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.DelegateNodeBranch;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.omitter.OmitterBranch;
import lexical.automata.omitter.TryOmitterBranch;
import lexical.automata.tokenizer.strategy.InitialNodeStrategy;
import lexical.automata.tokenizer.strategy.TokenizerNodeStrategy;
import org.jetbrains.annotations.NotNull;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Queue;

public class InitialTokenizerNode extends DefaultTokenizerNode {

    private final NodeBranchContainer<OmitterBranch> omitterBranches = new NodeBranchContainer<>();
    private final NodeBranchContainer<TryOmitterBranch> tryOmitterBranches = new NodeBranchContainer<>();

    public InitialTokenizerNode() {
        super(new InitialNodeStrategy());
    }

    /**
     * Adds a new {@link OmitterBranch} to this node.
     * <br>
     * If this node receives a character that matches this branch, then this node will delegate to it, thus skipping
     * a certain pattern of characters.
     * This process will repeat until no branch matches the next character emitted by {@link SourceCodeReader}.
     *
     * @param omitterBranch an {@link OmitterBranch} that connects this node with another one
     */
    public void addBranch(OmitterBranch omitterBranch){
        omitterBranches.addBranch(omitterBranch);
    }

    /**
     * Adds a new {@link TryOmitterBranch} to this node.
     * <br>
     * If this node receives a character that matches this branch, then this node will try to delegate to it, thus skipping
     * a certain pattern of characters. If an exception occurs during delegation, then this branch will be skipped and
     * the {@link SourceCodeReader} will be set to the initial state.
     * This process will repeat until no branch matches the next character emitted by {@link SourceCodeReader}.
     * <br><br>
     * You can assume that this branches will be the first of all to be tested against any character.
     *
     * @param tryOmitterBranch an {@link TryOmitterBranch} that connects this node with another one
     */
    public void addTryBranch(TryOmitterBranch tryOmitterBranch){
        tryOmitterBranches.addBranch(tryOmitterBranch);
    }

    @Override
    public @NotNull Token tokenize(@NotNull SourceCodeReader reader) throws UncheckedIOException, LexicalException {
        boolean didSkipCharacters;
        do {
            didSkipCharacters = reader.peek()
                                    .map(character -> testOmitterBranches(reader, character))
                                    .orElse(false);
        }while (didSkipCharacters);
        try {
            return super.tokenize(reader);
        } catch (LexicalException e){
            reader.next();
            throw e;
        }
    }

    private boolean testOmitterBranches(SourceCodeReader reader, CodeCharacter character){
        return testTryOmitterBranches(reader, character) || testCommonOmitterBranches(reader, character);
    }

    private boolean testTryOmitterBranches(SourceCodeReader reader, CodeCharacter character){
        Queue<TryOmitterBranch> matching = tryOmitterBranches.getAllMatchingBranches(character);
        for (TryOmitterBranch branch : matching){
            if (branch.tryDelegate(reader)){
                return true;
            }
        }
        return false;
    }

    private boolean testCommonOmitterBranches(SourceCodeReader reader, CodeCharacter character){
        Optional<OmitterBranch> selected = omitterBranches.getMatchingBranch(character);
        if (selected.isPresent()){
            selected.get().delegate(reader);
            return true;
        }
        return false;
    }
}
