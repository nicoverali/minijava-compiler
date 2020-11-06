import lexical.Token;
import semantic.SemanticException;
import syntactic.SyntacticException;

import java.io.PrintStream;

public class SemanticErrorPrinter extends AbstractErrorPrinter {

    private final PrintStream out;

    public SemanticErrorPrinter(PrintStream out) {
        super(out);
        this.out = out;
    }

    public void printError(SemanticException exception){
        Token token = exception.getExceptionToken();
        printDescription(exception);
        printDetail(token);
        printErrorCode(token);
    }

    private void printDescription(SemanticException exception){
        int line = exception.getExceptionToken().getLineNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Semantico en linea "+line+": "+errorMsg);
    }

}
