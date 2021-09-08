package lexical.automata;

import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.filter.LexicalFilter;

/**
 * A branch that connects two {@link LexicalNode} together.
 * It won't expose the next node directly, but provides a way of delegating to it.
 * <br>
 * All branches have an associated {@link LexicalFilter} to determine if a character can be delegated or not.
 *
 * @param <T> type of element return by the {@link LexicalNode} it connects
 */
public interface NodeBranch<T> {

    /**
     * Sets the filter of this branch. Only the characters that passes the filter will be delegated.
     *
     * @param filter a {@link LexicalFilter} which will be used by this branch
     */
    void setFilter(LexicalFilter filter);

    /**
     * Sets the next node of this branch. If given a certain character, it passes this branch filter, then the
     * processing of that character will be delegated to <code>nextNode</code>.
     *
     * @param nextNode the next {@link LexicalNode} of this branch
     */
    void setNextNode(LexicalNode<T> nextNode);

    /**
     * Test the next character of the {@link SourceCodeReader} and if it passes this branch {@link LexicalFilter}
     * then it delegates the processing of characters to the next node of this branch.
     * The branch will consume the next character only if it will actually delegate the processing, so that
     * the next node process the one after it.
     * <br><br>
     * The branch will return the resulting element of the next node processing, or null if the next node could not
     * process the next characters. Likewise, if the next node detects a lexical error then this branch will
     * propagate the exception.
     *
     * @see #setFilter(LexicalFilter)
     * @param reader a {@link SourceCodeReader} to take its next characters as input
     * @return an element of type <code>T</code> returned by the next node, or null if the next node couldn't process
     * the next character
     * @throws LexicalException if a lexical error is detected
     */
    T delegate(SourceCodeReader reader) throws LexicalException;


}
