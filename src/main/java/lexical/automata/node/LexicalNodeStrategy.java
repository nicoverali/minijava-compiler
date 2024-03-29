package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;

/**
 * The strategy of a {@link LexicalNode} determines what it should do when the processing of
 * characters cannot be delegated.
 *
 * @param <T> type of element returned by the strategy
 */
public interface LexicalNodeStrategy<T> {

    /**
     * Determines what to do in case no branch can match a certain character.
     * <br>
     * It will return an element of type <code>T</code> if it can generate one, or a <code>null</code> value
     * <br><br>
     * If a lexical error is detected then it will throw a {@link LexicalException}
     *
     *
     * @param reader the {@link SourceCodeReader} used by the node
     * @param currentCharacter the current {@link CodeCharacter}
     * @return an element of type <code>T</code> generated by the node strategy, or <code>null</code>
     * @throws LexicalException if the node detects a lexical error
     */
    T onNoBranchSelected(SourceCodeReader reader, CodeCharacter currentCharacter) throws LexicalException;

    /**
     * Similar to {@link #onNoBranchSelected}, but determines what to do when the end of file is reached.
     *
     *
     * @param reader the {@link SourceCodeReader} used by the node
     * @param currentLine the current {@link CodeLine}
     * @return an element of type <code>T</code> generated by the node strategy, or <code>null</code>
     * @throws LexicalException if the node detects a lexical error
     */
    T onEndOfFile(SourceCodeReader reader, CodeLine currentLine) throws LexicalException;
}
