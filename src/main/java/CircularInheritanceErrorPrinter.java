import lexical.Lexeme;
import lexical.Token;
import semantic.CircularInheritanceException;
import semantic.SemanticException;
import util.Iterables;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class CircularInheritanceErrorPrinter extends AbstractErrorPrinter {

    private final PrintStream out;

    public CircularInheritanceErrorPrinter(PrintStream out) {
        super(out);
        this.out = out;
    }

    public void printError(CircularInheritanceException exception){
        Token lastToken = Iterables.getLast(exception.getInvolvedTokens());
        printDescription(exception);
        printDetail(lastToken);
        printErrorCode(lastToken);
    }

    private void printDescription(CircularInheritanceException exception){
        List<String> involvedClasses = exception.getInvolvedTokens().stream()
                                    .map(Token::getLexeme)
                                    .map(Lexeme::toString)
                                    .collect(Collectors.toList());
        String allClases = String.join(", ", involvedClasses);
        out.println("Error de herencia circular, las entidades afectadas son : "+allClases);
    }

}
