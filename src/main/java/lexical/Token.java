package lexical;

import io.code.CodeCharacter;
import io.code.CodeLine;

import java.util.Optional;

public interface Token {

    /**
     * @return the {@link TokenType} of this Token
     */
    TokenType getType();

    /**
     * @return the lexeme that matches this Token pattern
     */
    String getLexeme();

    /**
     * Returns the {@link CodeLine} where this Token begins.
     * A Token usually has a {@link CodeLine} where it begins, in case this is {@link TokenType#EOF} then the first
     * line will be the last line of the file.
     * <br>
     * If the source code is empty then the Token won't have a first line and this method will simply return an
     * empty {@link Optional}
     *
     * @return an {@link Optional} wrapping the {@link CodeLine} in the source code where this particular Token begins
     */
    Optional<CodeLine> getFirstLine();

    /**
     * Returns the first {@link CodeCharacter} of this particular Token.
     * <br>
     * If the Token is a {@link TokenType#EOF} then it won't have a first character, in which case this method will
     * return an empty {@link Optional}
     *
     * @return an {@link Optional} wrapping the first {@link CodeCharacter} of this particular Token
     */
    Optional<CodeCharacter> getFirstCharacter();

    /**
     * @return the source code line number where this Token begins
     */
    int getLineNumber();

    /**
     * @see #getFirstLine()
     * @return the column number where this particular Token begins in the {@link CodeLine} that contains it
     */
    int getColumnNumber();

}
