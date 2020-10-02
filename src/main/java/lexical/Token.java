package lexical;

public class Token {

    private TokenType type;
    private String lexeme;
    private int lineNumber;

    public Token(TokenType type, String lexeme, int lineNumber) {
        this.type = type;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
