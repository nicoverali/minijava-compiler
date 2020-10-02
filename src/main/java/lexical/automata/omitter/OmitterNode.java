package lexical.automata.omitter;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.omitter.strategy.OmitterNodeStrategy;

import java.io.UncheckedIOException;
import java.util.Optional;

public class OmitterNode implements LexicalNode<OmitterBranch> {

    private final OmitterNodeStrategy strategy;
    private final NodeBranchContainer<OmitterBranch> branches;

    public OmitterNode(OmitterNodeStrategy strategy, NodeBranchContainer<OmitterBranch> branches) {
        this.strategy = strategy;
        this.branches = branches;
    }

    @Override
    public void addBranch(OmitterBranch branch) {
        branches.addBranch(branch);
    }

    /**
     * Takes as input the next characters from the given {@link SourceCodeReader},
     * and omits (consumes) them if they match a particular pattern.
     * <br>
     * If it can't match the pattern, then a {@link LexicalException} will be thrown
     * <br><br>
     * To match the characters with a pattern, it will use its attached {@link NodeBranch}
     *
     * @see #addBranch(OmitterBranch)
     *
     * @param codeReader a {@link SourceCodeReader} to take its next characters as input
     * @throws UncheckedIOException if an I/O occurs
     * @throws LexicalException if the node couldn't match the characters with a pattern
     */
    public void omit(SourceCodeReader codeReader) throws UncheckedIOException, LexicalException{
        if (!codeReader.hasNext()){
            strategy.onEndOfFile(codeReader.getCurrentLine().orElse(null));
            return;
        }

        Optional<OmitterBranch> selectedBranch = codeReader.peekNext().flatMap(branches::selectBranch);
        if (selectedBranch.isPresent()){
            delegate(codeReader, selectedBranch.get());
        } else {
            //noinspection OptionalGetWithoutIsPresent (already checked with hasNext)
            strategy.onNoBranchSelected(codeReader.peekNext().get());
        }
    }

    private void delegate(SourceCodeReader reader, OmitterBranch selectedBranch) throws UncheckedIOException {
        reader.getNext(); // Consume character
        selectedBranch.getNextNode().omit(reader);
    }

}
