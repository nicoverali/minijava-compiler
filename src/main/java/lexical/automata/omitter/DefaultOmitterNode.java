package lexical.automata.omitter;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.omitter.strategy.OmitterNodeStrategy;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Queue;

public class DefaultOmitterNode implements OmitterNode {

    private final OmitterNodeStrategy strategy;
    private final NodeBranchContainer<TryOmitterBranch> tryBranches = new NodeBranchContainer<>();
    private final NodeBranchContainer<OmitterBranch> branches = new NodeBranchContainer<>();

    public DefaultOmitterNode(OmitterNodeStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void addBranch(OmitterBranch branch) {
        branches.addBranch(branch);
    }

    @Override
    public void addTryBranch(TryOmitterBranch branch) {
        tryBranches.addBranch(branch);
    }

    @Override
    public void omit(SourceCodeReader codeReader) throws UncheckedIOException, LexicalException{
        Optional<CodeCharacter> nextChar = codeReader.peek();
        if (nextChar.isPresent()){
            testAllBranches(codeReader, nextChar.get());
        } else {
            strategy.onEndOfFile(codeReader.getCurrentLine().orElse(null));
        }
    }

    private void testAllBranches(SourceCodeReader reader, CodeCharacter character){
        boolean handled = testTryBranches(reader, character) || testCommonBranches(reader, character);
        if (!handled) {
            strategy.onNoBranchSelected(character);
        }
    }

    private boolean testTryBranches(SourceCodeReader reader, CodeCharacter character){
        Queue<TryOmitterBranch> matching = tryBranches.getAllMatchingBranches(character);
        for (TryOmitterBranch branch : matching) {
            if (branch.tryDelegate(reader)){
                return true;
            }
        }
        return false;
    }

    private boolean testCommonBranches(SourceCodeReader reader, CodeCharacter character){
        Optional<OmitterBranch> selected = branches.getMatchingBranch(character);
        selected.ifPresent(branch -> branch.delegate(reader));
        return selected.isPresent();
    }

}
