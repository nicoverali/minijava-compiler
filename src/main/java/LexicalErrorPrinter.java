import lexical.LexicalException;

import java.io.PrintStream;

public class LexicalErrorPrinter extends AbstractErrorPrinter {

    private final PrintStream out;

    public LexicalErrorPrinter(PrintStream out){
        super(out);
        this.out = out;
    }

    public void printError(LexicalException exception){
        printDescription(exception);
        printDetail(exception.getLine(), exception.getColumnNumber());
        printErrorCode(exception.getLexeme().toString(), exception.getLineNumber());
    }

    private void printDescription(LexicalException exception){
        int line = exception.getLineNumber()+1;
        String errorMsg = exception.getMessage();
        out.println("Error Léxico en linea "+line+": "+errorMsg);
    }


}
