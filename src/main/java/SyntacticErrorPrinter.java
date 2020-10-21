import com.google.common.base.Strings;
import io.code.CodeLine;
import lexical.LexicalException;
import syntactic.SyntacticException;
import util.Characters;

import java.io.PrintStream;

public class SyntacticErrorPrinter {

    private static final String DETAIL_PREFIX = "Detalle: ";
    private static final String INITIAL_PADDING = Strings.padStart("", DETAIL_PREFIX.length(), ' ');

    private final PrintStream out;

    public SyntacticErrorPrinter(PrintStream out){
        this.out = out;
    }

    public void printError(SyntacticException exception){
        printDescription(exception);
        printDetail(exception);
        printErrorCode(exception);
    }

    private void printDescription(SyntacticException exception){
        int line = exception.getExceptionToken().getLineNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Sintáctico en linea "+line+": "+errorMsg);
    }

    private void printDetail(SyntacticException exception){
        String tokenLine = exception.getExceptionToken().getFirstLine().map(CodeLine::toString).orElse("");
        String line = Characters.removeNewLine(tokenLine);
        String indicator = createIndicator(line, exception.getExceptionToken().getColumnNumber());

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

    private void printErrorCode(SyntacticException exception){
        int line = exception.getExceptionToken().getLineNumber()+1;
        String lexeme = exception.getExceptionToken().getLexeme().toString();
        out.println("[Error:"+lexeme+"|"+line+"]");
    }



}
