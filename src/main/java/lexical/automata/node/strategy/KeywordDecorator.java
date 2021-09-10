package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import lexical.automata.Lexeme;

import java.util.HashMap;
import java.util.Map;

import static lexical.TokenType.*;

public class KeywordDecorator implements LexicalNodeStrategy {

    private static final Map<String, TokenType> keywordMap = new HashMap<>();
    static {
        keywordMap.put("class", K_CLASS);
        keywordMap.put("extends", K_EXTENDS);
        keywordMap.put("static", K_STATIC);
        keywordMap.put("dynamic", K_DYNAMIC);
        keywordMap.put("void", K_VOID);
        keywordMap.put("boolean", K_BOOLEAN);
        keywordMap.put("char", K_CHAR);
        keywordMap.put("int", K_INT);
        keywordMap.put("String", K_STRING);
        keywordMap.put("public", K_PUBLIC);
        keywordMap.put("private", K_PRIVATE);
        keywordMap.put("if", K_IF);
        keywordMap.put("else", K_ELSE);
        keywordMap.put("while", K_WHILE);
        keywordMap.put("return", K_RETURN);
        keywordMap.put("this", K_THIS);
        keywordMap.put("new", K_NEW);
        keywordMap.put("null", K_NULL);
        keywordMap.put("true", K_TRUE);
        keywordMap.put("false", K_FALSE);
        keywordMap.put("interface", K_INTERFACE);
        keywordMap.put("implements", K_IMPLEMENTS);
    }

    private final LexicalNodeStrategy decorated;

    public KeywordDecorator(LexicalNodeStrategy strategy) {
        this.decorated = strategy;
    }

    @Override
    public AutomataToken onNoBranchSelected(SourceCodeReader reader, Lexeme currentLexeme, CodeCharacter nextCharacter) throws LexicalException {
        AutomataToken token = decorated.onNoBranchSelected(reader, currentLexeme, nextCharacter);
        applyMap(token);
        return token;
    }

    @Override
    public AutomataToken onEndOfFile(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        AutomataToken token = decorated.onEndOfFile(reader, currentLexeme);
        applyMap(token);
        return token;
    }

    private void applyMap(AutomataToken token){
        if (token != null){
            TokenType keywordType = keywordMap.get(token.getLexeme());
            if (keywordType != null){
                token.setType(keywordType);
            }
        }
    }
}
