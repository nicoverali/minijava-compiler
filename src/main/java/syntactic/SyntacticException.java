package syntactic;

import lexical.Token;

public class SyntacticException extends RuntimeException{

    private final Token exceptionToken;

    public SyntacticException(String message, Token exceptionToken) {
        super(message);
        this.exceptionToken = exceptionToken;
    }

    public Token getExceptionToken(){
        return exceptionToken;
    }
}
