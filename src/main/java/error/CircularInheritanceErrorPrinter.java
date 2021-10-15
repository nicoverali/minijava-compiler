package error;

import lexical.Token;
import semantic.CircularInheritanceException;
import util.Iterables;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class CircularInheritanceErrorPrinter extends AbstractErrorPrinter<CircularInheritanceException> {

    public void printError(CircularInheritanceException exception){
        Token lastToken = Iterables.getLast(exception.getInvolvedTokens());
        printDescription(exception);
        printDetail(lastToken);
        printErrorCode(lastToken);
    }

    private void printDescription(CircularInheritanceException exception){
        List<String> involvedClasses = exception.getInvolvedTokens().stream()
                                    .map(Token::getLexeme)
                                    .collect(Collectors.toList());

        String allClasses = String.join(", ", involvedClasses);
        out.println("Error de herencia circular, las entidades afectadas son : "+allClasses);
    }

}
