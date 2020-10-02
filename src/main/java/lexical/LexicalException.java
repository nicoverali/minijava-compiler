package lexical;

import io.code.CodeLine;

public class LexicalException extends RuntimeException{

    private final CodeLine exceptionLine;
    private final int columnNumber;

    public LexicalException(String msg, int columnNumber, CodeLine exceptionLine){
        super(msg);
        this.columnNumber = columnNumber;
        this.exceptionLine = exceptionLine;
    }

    /**
     * @return the column number at which the lexical error occur
     * @see #getExceptionLine()
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * @return the {@link CodeLine} at which the lexical error occur
     */
    public CodeLine getExceptionLine() {
        return exceptionLine;
    }

}
