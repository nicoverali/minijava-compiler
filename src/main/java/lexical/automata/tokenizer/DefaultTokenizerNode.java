package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.tokenizer.strategy.TokenizerNodeStrategy;
import org.jetbrains.annotations.NotNull;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Queue;

public class DefaultTokenizerNode implements TokenizerNode {

    private final NodeBranchContainer<TryTokenizerBranch> tryBranches = new NodeBranchContainer<>();
    private final NodeBranchContainer<TokenizerBranch> branches = new NodeBranchContainer<>();
    private final TokenizerNodeStrategy strategy;

    public DefaultTokenizerNode(TokenizerNodeStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void addBranch(TokenizerBranch branch) {
        branches.addBranch(branch);
    }

    @Override
    public void addTryBranch(TryTokenizerBranch branch) {
        tryBranches.addBranch(branch);
    }

    @Override
    public @NotNull Token tokenize(@NotNull SourceCodeReader reader) throws UncheckedIOException, LexicalException {
        Optional<CodeCharacter> nextChar = reader.peek();
        if (nextChar.isPresent()){
            return testAllBranches(reader, nextChar.get());
        }
        return strategy.onEndOfFile(reader.getCurrentLine().orElse(null));
    }

    private Token testAllBranches(SourceCodeReader reader, CodeCharacter character){
        Token result;
        if ((result = testTryBranches(reader, character)) != null){
            return result;
        } else if ((result = testCommonBranches(reader, character)) != null){
            return result;
        } else {
            return strategy.onNoBranchSelected(character);
        }
    }

    private Token testTryBranches(SourceCodeReader reader, CodeCharacter character){
        Queue<TryTokenizerBranch> matching = tryBranches.getAllMatchingBranches(character);
        Token token = null;
        for (TryTokenizerBranch branch : matching){
            if ((token = branch.tryDelegate(reader)) != null){
                break;
            }
        }
        return token;
    }

    private Token testCommonBranches(SourceCodeReader reader, CodeCharacter character){
        return branches.getMatchingBranch(character)
                .map(branch -> branch.delegate(reader))
                .orElse(null);
    }
}
