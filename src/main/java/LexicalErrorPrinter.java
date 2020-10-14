import com.google.common.base.Strings;
import lexical.LexicalException;
import util.Characters;

import java.io.PrintStream;

public class LexicalErrorPrinter {

    private static final String DETAIL_PREFIX = "Detalle: ";
    private static final String INITIAL_PADDING = Strings.padStart("", DETAIL_PREFIX.length(), ' ');

    private final PrintStream out;

    public LexicalErrorPrinter(PrintStream out){
        this.out = out;
    }

    public void printError(LexicalException exception){
        printDescription(exception);
        printDetail(exception);
        printErrorCode(exception);
    }

    private void printDescription(LexicalException exception){
        int line = exception.getLineNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Léxico en linea "+line+": "+errorMsg);
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
        String lexeme = exception.getLexeme().toString();
        out.println("[Error:"+lexeme+"|"+line+"]");
    }



}
