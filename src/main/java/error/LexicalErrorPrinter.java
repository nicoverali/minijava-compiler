package error;

import lexical.LexicalException;

import static java.lang.System.out;

public class LexicalErrorPrinter extends AbstractErrorPrinter {

    public void printError(LexicalException exception){
        printDescription(exception);
        printDetail(exception.getLine(), exception.getColumnNumber());
        printErrorCode(exception.getLexeme(), exception.getLineNumber());
    }

    private void printDescription(LexicalException exception){
        int line = exception.getLineNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Léxico en linea "+line+": "+errorMsg);
    }


}
