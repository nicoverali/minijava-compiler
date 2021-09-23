package error;

import lexical.Token;
import syntactic.SyntacticException;

import java.io.PrintStream;

public class SyntacticErrorPrinter extends AbstractErrorPrinter{

    private final PrintStream out;

    public SyntacticErrorPrinter(){
        this.out = System.out;
    }

    public void printError(SyntacticException exception){
        Token token = exception.getExceptionToken();
        printDescription(exception);
        printDetail(token);
        printErrorCode(token);
    }

    private void printDescription(SyntacticException exception){
        int line = exception.getExceptionToken().getLineNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Sintáctico en linea "+line+": "+errorMsg);
    }



}
