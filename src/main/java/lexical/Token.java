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
     * Sets the type of this Token
     *
     * @param type a {@link TokenType} which will be the new type of this Token
     */
    void setType(TokenType type);

    /**
     * Adds the given {@link CodeCharacter} at the beginning of this Token.
     *
     * @param character a {@link CodeCharacter} that will be add at the beginning of this Token
     */
    void prepend(CodeCharacter character);

    /**
     * @return the lexeme that matches this Token pattern
     */
    String getLexeme();

    /**
     * Prepends the Lexeme of this Token with the given {@link CodeCharacter}.
     * @param character a {@link CodeCharacter} which will be prepended to this Token's Lexeme
     */
    void prependLexeme(CodeCharacter character);

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
     * If the Token is a {@link TokenType#EOF} the it won't have a first character, in which case this method will
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
