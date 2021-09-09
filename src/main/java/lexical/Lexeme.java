package lexical;

import io.code.CodeCharacter;
import io.code.CodeLine;

import java.util.Optional;

public interface Lexeme {


    /**
     * @return this lexeme as a String
     */
    @Override
    String toString();

    /**
     * @return true if this Lexeme has not characters, false otherwise
     */
    boolean isEmpty();

    /**
     * Adds the given {@link CodeCharacter} at the beginning of this Lexeme
     *
     * @param character a {@link CodeCharacter} which will be added to the beginning of this Lexeme
     */
    void prepend(CodeCharacter character);

    /**
     * Returns the {@link CodeLine} where this Lexeme begins, or an empty {@link Optional} if the Lexeme is empty
     *
     * @return an {@link Optional} wrapping the {@link CodeLine} where this Lexeme begins
     */
    Optional<CodeLine> getFirstLine();

    /**
     * Returns the first {@link CodeCharacter} of this Lexeme, or an empty {@link Optional} if the Lexeme is empty
     *
     * @return an {@link Optional} wrapping the first {@link CodeCharacter} of this Lexeme
     */
    Optional<CodeCharacter> getFirstCharacter();

    /**
     * Returns the source code line number where this Lexeme begins. If the source code is empty, then this
     * method will return 0.
     *
     * @return the source code line number where this Lexeme begins,  or 0 if the source code is empty
     */
    int getLineNumber();

    /**
     * Returns the column number of the {@link #getFirstCharacter()} of this Lexeme.
     * <br>
     * If the Lexeme does not have a first character because {@link #isEmpty()}, then this method
     * will return as column number the size of the first line, that is, the first position outside the line end bound.
     * This implies that if the source code is empty, then the method returns 0.
     *
     * @see #getFirstLine()
     * @return the column number where this Lexeme begins at its starting line
     */
    int getColumnNumber();

}
