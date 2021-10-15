package semantic;

import lexical.Token;

import java.util.Collections;
import java.util.List;

public class CircularInheritanceException extends RuntimeException {

    private final List<Token> involved;

    public CircularInheritanceException(String message, List<Token> involved) {
        super(message);
        this.involved = involved;
    }

    public List<Token> getInvolvedTokens(){
        return Collections.unmodifiableList(involved);
    }
}
