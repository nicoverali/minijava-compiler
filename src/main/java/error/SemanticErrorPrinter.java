package error;

import lexical.Token;
import semantic.SemanticException;

import static java.lang.System.out;

public class SemanticErrorPrinter extends AbstractErrorPrinter<SemanticException> {

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
