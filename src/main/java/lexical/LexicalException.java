package lexical;

public class LexicalException extends RuntimeException{

    private final Lexeme lexeme;
    private final String line;
    private final int lineNumber;
    private final int columnNumber;

    public LexicalException(String message, Lexeme lexeme, String line, int lineNumber, int columnNumber) {
        super(message);
        this.lexeme = lexeme;
        this.line = line;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    /**
     * @return the current Lexeme at the moment this {@link LexicalException} occur
     */
    public Lexeme getLexeme(){
        return lexeme;
    }

    /**
     * @return the line at which the lexical error occur
     */
    public String getLine() {
        return line;
    }

    /**
     * @return the number of line where this exception occur
     */
    public int getLineNumber(){
        return lineNumber;
    }

    /**
     * @return the column number at which the lexical error occur
     * @see #getLine()
     */
    public int getColumnNumber() {
        return columnNumber;
    }


}
