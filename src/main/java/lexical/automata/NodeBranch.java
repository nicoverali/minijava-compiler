package lexical.automata;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.branch.filter.LexicalFilter;

/**
 * A branch that connects two {@link LexicalNode} together.
 * It won't expose the next node directly, but provides a way of delegating to it.
 * <br>
 * All branches have an associated {@link LexicalFilter} to determine if a character can be delegated or not.
 */
public interface NodeBranch {

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
    void setNextNode(LexicalNode nextNode);

    /**
     * Determines whether the given character passes this branch filter. If so, is safe to delegate
     * the process of characters to the next node of this branch.
     *
     * @param character the character to be tested
     * @return true if the given character passes this branch filter, false otherwise
     */
    boolean test(CodeCharacter character);

    /**
     * Delegates the processing of characters to the next node of this branch.
     * The branch will consume the next character and add it to the current lexeme.
     * The next node will process the one after it.
     * <br><br>
     * The branch will return the resulting element of the next node processing.
     * Likewise, if the next node detects a lexical error then this branch will
     * propagate the exception.
     *
     * @see #setFilter(LexicalFilter)
     * @param reader a {@link SourceCodeReader} to take its next characters as input
     * @param currentLexeme the current lexeme
     * @return a nullable {@link Token} returned by the next node
     * @throws LexicalException if a lexical error is detected
     */
    Token delegate(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException;


}
