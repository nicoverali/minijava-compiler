package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import lexical.LexicalException;
import lexical.TokenType;
import lexical.automata.Lexeme;

public class MaxDigitStrategy extends AcceptorStrategy{

    private final String errorMsg;

    public MaxDigitStrategy(String errorMsg) {
        super(TokenType.INT);
        this.errorMsg = errorMsg;
    }

    public void onMaxDigitReached(Lexeme currentLexeme, CodeCharacter nextChar) throws LexicalException{
        String line = nextChar.getCodeLine().toString();
        int lineNumber = nextChar.getLineNumber();
        int columnNumber = nextChar.getColumnNumber();
        throw new LexicalException(errorMsg, currentLexeme.toString(), line, lineNumber, columnNumber);
    }

}
