package lexical;

public class Token {

    private TokenType type;
    private Lexeme lexeme;

    public Token(TokenType type, Lexeme lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return type;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    public int getLineNumber() {
        return lexeme.getLineNumber();
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void setLexeme(Lexeme lexeme) {
        this.lexeme = lexeme;
    }
}
