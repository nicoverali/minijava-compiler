package error;

import semantic.MainMethodException;

import static java.lang.System.out;

public class MainMethodErrorPrinter extends AbstractErrorPrinter<MainMethodException> {

    @Override
    public void printError(MainMethodException exception) {
        out.println("Error semantico: " + exception.getMessage());
        exception.getExceptionToken().ifPresent(this::printDetail);
        exception.getExceptionToken().ifPresentOrElse(this::printErrorCode, () -> out.println("\n[Error:main|]"));
    }

}
