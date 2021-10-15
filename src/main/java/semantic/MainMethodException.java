package semantic;

import lexical.Token;
import semantic.symbol.Symbol;

import java.util.Optional;

public class MainMethodException extends RuntimeException{

    private Token exceptionToken;

    public MainMethodException(String message) {
        super(message);
    }

    public MainMethodException(String message, Token exceptionToken) {
        super(message);
        this.exceptionToken = exceptionToken;
    }

    public MainMethodException(String message, Symbol symbol) {
        super(message);
        this.exceptionToken = symbol.getNameAttribute().getToken();
    }

    public Optional<Token> getExceptionToken() {
        return Optional.ofNullable(exceptionToken);
    }
}
