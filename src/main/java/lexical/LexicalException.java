package lexical;

import io.code.CodeCharacter;
import io.code.CodeLine;

public class LexicalException extends RuntimeException {

    private String lexeme;
    private final String line;
    private final int lineNumber;
    private final int columnNumber;

    public LexicalException(String message, CodeLine line, String lexeme, int columnNumber) {
        super(message);
        this.lexeme = lexeme;
        this.line = line != null ? line.toString() : "";
        this.lineNumber = line != null ? line.getLineNumber() : 0;
        this.columnNumber = columnNumber;
    }

    /**
     * @return the current Lexeme at the moment this {@link LexicalException} occur
     */
    public String getLexeme(){
        return lexeme;
    }

    /**
     * Prepends the Lexeme of this exception with the given {@link CodeCharacter}.
     *
     * @param character a {@link CodeCharacter} which will be prepended to this exception lexeme
     */
    public void prependLexeme(CodeCharacter character){
        lexeme = character.getValue() + lexeme;
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
