package lexical;

import io.code.CodeCharacter;
import io.code.CodeLine;

public class LexicalException extends RuntimeException {

    private final Lexeme lexeme;
    private final String line;
    private final int lineNumber;
    private final int columnNumber;

    public LexicalException(String message, Lexeme lexeme, int columnNumber) {
        super(message);
        this.lexeme = lexeme;
        this.line = lexeme.getFirstLine().map(CodeLine::toString).orElse("");
        this.lineNumber = lexeme.getLineNumber();
        this.columnNumber = columnNumber;
    }

    /**
     * @return the current Lexeme at the moment this {@link LexicalException} occur
     */
    public Lexeme getLexeme(){
        return lexeme;
    }

    /**
     * Prepends the Lexeme of this exception with the given {@link CodeCharacter}.
     *
     * @see Lexeme#prepend(CodeCharacter)
     * @param character a {@link CodeCharacter} which will be prepend to this exception lexeme
     */
    public void prependLexeme(CodeCharacter character){
        lexeme.prepend(character);
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
