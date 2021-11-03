package error;

import semantic.UnreachableCodeException;

import static java.lang.System.out;

public class UnreachableCodeErrorPrinter extends AbstractErrorPrinter<UnreachableCodeException> {

    @Override
    public void printError(UnreachableCodeException exception) {
        out.println("Error semantico: " + exception.getMessage());
        out.println("\n[Error:"+exception.getExceptionToken().getLexeme()+"|"+(exception.getLineNumber()+1)+"]");
    }

}
