package lexical.automata.omitter;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;

import java.io.UncheckedIOException;

public interface OmitterNode extends LexicalNode<OmitterBranch, TryOmitterBranch> {

    /**
     * Takes as input the next characters from the given {@link SourceCodeReader},
     * and omits (consumes) them if they match a particular pattern.
     * <br>
     * If it can't match the pattern, then a {@link LexicalException} will be thrown
     * <br><br>
     * To match the characters with a pattern, it will use its attached {@link NodeBranch}. First all try-branches
     * will be tested, then common branches
     *
     * @see #addBranch 
     * @see #addTryBranch
     *
     * @param codeReader a {@link SourceCodeReader} to take its next characters as input
     * @throws UncheckedIOException if an I/O occurs
     * @throws LexicalException if the node couldn't match the characters with a pattern
     */
    void omit(SourceCodeReader codeReader) throws UncheckedIOException, LexicalException;
    
}
