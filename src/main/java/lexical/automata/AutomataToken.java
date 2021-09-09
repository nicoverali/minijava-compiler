package lexical.automata;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Token;
import lexical.TokenType;

import java.util.Optional;

public class AutomataToken implements Token {

    private String lexeme;
    private TokenType type;
    private CodeCharacter firstCharacter;
    private CodeLine firstLine;


    public AutomataToken(TokenType type, String lexeme, CodeCharacter firstCharacter) {
        this.type = type;
        this.lexeme = lexeme;
        this.firstCharacter = firstCharacter;
        this.firstLine = firstCharacter != null
            ? firstCharacter.getCodeLine()
            : null;
    }

    public AutomataToken(TokenType type, String lexeme, CodeLine line) {
        this.type = type;
        this.lexeme = lexeme;
        this.firstCharacter = null;
        this.firstLine = line;
    }

    /**
     * Sets the type of this Token
     *
     * @param type a {@link TokenType} which will be the new type of this Token
     */
    public void setType(TokenType type){
        this.type = type;
    }

    /**
     * Adds the given {@link CodeCharacter} at the beginning of this Token.
     *
     * @param character a {@link CodeCharacter} that will be add at the beginning of this Token
     */
    public void prepend(CodeCharacter character){
        firstCharacter = character;
        firstLine = character.getCodeLine();
    }

    /**
     * Prepends the Lexeme of this Token with the given {@link CodeCharacter}.
     * @param character a {@link CodeCharacter} which will be prepended to this Token's Lexeme
     */
    public void prependLexeme(CodeCharacter character){
        lexeme = character.getValue() + lexeme;
    }

    @Override
    public TokenType getType() {
        return type;
    }


    @Override
    public String getLexeme() {
        return lexeme;
    }


    @Override
    public Optional<CodeLine> getFirstLine() {
        return Optional.ofNullable(firstLine);
    }

    @Override
    public Optional<CodeCharacter> getFirstCharacter() {
        return Optional.ofNullable(firstCharacter);
    }

    @Override
    public int getLineNumber() {
        return firstLine != null
                ? firstLine.getLineNumber()
                : 0;
    }

    @Override
    public int getColumnNumber() {
        if (firstCharacter != null){
            return firstCharacter.getColumnNumber();
        } else if (firstLine != null) {
            return firstLine.getSize();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "(" + type + "," + lexeme + "," + getLineNumber() + ")";
    }
}
