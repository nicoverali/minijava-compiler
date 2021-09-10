package lexical.automata;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Token;
import lexical.TokenType;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AutomataToken implements Token {

    private final Lexeme lexeme;
    private final CodeLine firstLine;
    private final CodeCharacter firstCharacter;
    private TokenType type;

    public AutomataToken(Lexeme lexeme, TokenType type, CodeLine firstLine, CodeCharacter firstCharacter) {
        this.lexeme = lexeme;
        this.type = type;
        this.firstLine = firstLine;
        this.firstCharacter = firstCharacter;
    }

    /**
     * Sets the type of this Token
     *
     * @param type a {@link TokenType} which will be the new type of this Token
     */
    public void setType(TokenType type){
        this.type = type;
    }

    @Override
    public TokenType getType() {
        return type;
    }


    @Override
    public String getLexeme() {
        return lexeme.toString();
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
        if (firstLine == null) return 0;
        return firstLine.getLineNumber();
    }

    @Override
    public int getColumnNumber() {
        if (firstCharacter == null) return 0;
        return firstCharacter.getColumnNumber();
    }

    @Override
    public String toString() {
        return "(" + type + "," + lexeme + "," + getLineNumber() + ")";
    }
}
