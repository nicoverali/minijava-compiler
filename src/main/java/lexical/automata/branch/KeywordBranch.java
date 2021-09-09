package lexical.automata.branch;

import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import lexical.automata.NodeBranch;

import java.util.HashMap;
import java.util.Map;

import static lexical.TokenType.*;

/**
 * This is a {@link NodeBranch} decorator, that in case the decorated branch
 * returns an {@link AutomataToken} os a result of delegation, it tries to map the token to a keyword token.
 * If the token cannot be mapped, then it simply returns the original token
 */
public class KeywordBranch extends NodeBranchDecorator {

    private static final Map<String, TokenType> keywordMap = new HashMap<>();
    static {
        keywordMap.put("class", K_CLASS);
        keywordMap.put("interface", K_INTERFACE);
        keywordMap.put("extends", K_EXTENDS);
        keywordMap.put("implements", K_IMPLEMENTS);
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
    }

    @Override
    public Token delegate(SourceCodeReader reader) throws LexicalException {
        return applyMap(decorated.delegate(reader));
    }

    private Token applyMap(Token sourceToken){
        if (sourceToken != null){
            TokenType keywordType = keywordMap.get(sourceToken.getLexeme().toString());
            if (keywordType != null){
                sourceToken.setType(keywordType);
            }
        }
        return sourceToken;
    }


    public String toString(){
        return "KeywordMap: " + decorated;
    }

}
