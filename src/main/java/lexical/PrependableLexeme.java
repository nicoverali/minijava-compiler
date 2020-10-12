package lexical;

import io.code.CodeCharacter;

public interface PrependableLexeme extends Lexeme {

    /**
     * Adds the given {@link CodeCharacter} at the beginning of this Lexeme
     *
     * @param character a {@link CodeCharacter} which will be added to the beginning of this Lexeme
     */
    void prepend(CodeCharacter character);

}
