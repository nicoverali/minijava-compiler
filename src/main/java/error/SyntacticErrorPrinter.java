package error;

import lexical.Token;
import syntactic.SyntacticException;

import static java.lang.System.out;

public class SyntacticErrorPrinter extends AbstractErrorPrinter{

    public void printError(SyntacticException exception){
        Token token = exception.getExceptionToken();
        printDescription(exception);
        printDetail(token);
        printErrorCode(token);
    }

    private void printDescription(SyntacticException exception){
        int line = exception.getExceptionToken().getLineNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Sintactico en linea "+line+": "+errorMsg);
    }



}
