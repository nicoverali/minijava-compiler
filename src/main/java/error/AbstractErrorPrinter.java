package error;

import io.code.CodeLine;
import lexical.Token;
import util.Characters;

import static java.lang.System.out;

public abstract class AbstractErrorPrinter<T extends Exception> {

    private static final String DETAIL_PREFIX = "Detalle: ";
    private static final String INITIAL_PADDING = padLeft("", DETAIL_PREFIX.length());

    abstract public void printError(T exception);

    protected void printDetail(Token token){
        this.printDetail(token.getFirstLine().map(CodeLine::toString).orElse(""), token.getColumnNumber());
    }

    protected void printDetail(String errorLine, int columnNumber){
        String line = Characters.removeNewLine(errorLine);
        String indicator = createIndicator(line, columnNumber);

        out.println(DETAIL_PREFIX+line);
        out.println(indicator);
    }

    private String createIndicator(String line, int indicatorColumn){
        StringBuilder indicator = new StringBuilder(INITIAL_PADDING);
        for (int i = 0; i < indicatorColumn && i < line.length(); i++) {
            if (line.charAt(i) == '\t')
                indicator.append('\t');
            else
                indicator.append(' ');
        }
        if (line.length() == indicatorColumn) indicator.append(' ');
        return indicator.append('^').toString();
    }

    protected void printErrorCode(Token exceptionToken){
        int lineNumber = exceptionToken.getLineNumber()+1;
        String lexeme = exceptionToken.getLexeme();
        this.printErrorCode(lexeme, lineNumber);
    }

    protected void printErrorCode(String lexeme, int lineNumber){
        out.println("[Error:"+lexeme+"|"+lineNumber+"]");
    }

    private static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

}
