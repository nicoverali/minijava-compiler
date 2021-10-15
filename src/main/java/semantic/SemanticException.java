package semantic;

import lexical.Token;
import semantic.symbol.Symbol;
import semantic.symbol.attribute.SymbolAttribute;

import java.util.Optional;

public class SemanticException extends RuntimeException{

    private final Token exceptionToken;

    public SemanticException(String message, SymbolAttribute<?> attribute) {
        super(message);
        this.exceptionToken = attribute.getToken();
        if (exceptionToken == null){
            throw new IllegalArgumentException("Attributes provided to SemanticException must have an associated Token");
        }
    }

    public SemanticException(String message, Symbol symbol) {
        super(message);
        this.exceptionToken = symbol.getNameAttribute().getToken();
        if (exceptionToken == null){
            throw new IllegalArgumentException("Attributes provided to SemanticException must have an associated Token");
        }
    }

    public SemanticException(String message, Token exceptionToken) {
        super(message);
        this.exceptionToken = exceptionToken;
    }

    /**
     * @return the source code {@link Token} that caused this exception
     */
    public Token getExceptionToken(){
        return exceptionToken;
    }

    /**
     * @see #getExceptionToken()
     * @return the lexeme associated with the {@link Token} that caused this exception
     */
    public String getLexeme(){
        return exceptionToken.getLexeme();
    }
}
