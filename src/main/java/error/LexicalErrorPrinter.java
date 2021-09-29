package error;

import lexical.LexicalException;
import util.Characters;

import static java.lang.System.out;

public class LexicalErrorPrinter {

    private static final String DETAIL_PREFIX = "Detalle: ";
    private static final String INITIAL_PADDING = padLeft("", DETAIL_PREFIX.length());

    public void printError(LexicalException exception){
        printDescription(exception);
        printDetail(exception);
        printErrorCode(exception);
    }

    private void printDescription(LexicalException exception){
        int line = exception.getLineNumber()+1;
        int column = exception.getColumnNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Léxico en linea "+line+", columna "+ column +": "+errorMsg);
    }

    private void printDetail(LexicalException exception){
        String line = Characters.removeNewLine(exception.getLine());
        String indicator = createIndicator(line, exception.getColumnNumber());

        out.println(DETAIL_PREFIX+line);
        out.println(indicator);
    }

    private String createIndicator(String line, int indicatorColumn){
        StringBuilder indicator = new StringBuilder(INITIAL_PADDING);
        for (int i = 0; i < indicatorColumn; i++) {
            if (line.charAt(i) == '\t')
                indicator.append('\t');
            else
                indicator.append(' ');
        }
        return indicator.append('^').toString();
    }

    private void printErrorCode(LexicalException exception){
        int line = exception.getLineNumber()+1;
        String lexeme = exception.getLexeme();
        out.println("[Error:"+lexeme+"|"+line+"]");
    }

    private static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }


}
