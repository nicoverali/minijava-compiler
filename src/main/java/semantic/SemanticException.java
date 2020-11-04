package semantic;

import lexical.Token;

public class SemanticException extends RuntimeException{

    private Token exceptionToken;

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
        return exceptionToken.getLexeme().toString();
    }
}
