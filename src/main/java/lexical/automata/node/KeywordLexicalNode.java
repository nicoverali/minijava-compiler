package lexical.automata.node;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static lexical.TokenType.*;

/**
 * This is a {@link LexicalNode} decorator, that in case the decorated node
 * returns a {@link AutomataToken} on processing, it tries to map the token to a keyword token.
 * If the token cannot be mapped, then it simply returns the original token
 */
public class KeywordLexicalNode implements LexicalNode<AutomataToken> {

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
    }

    private final LexicalNode<AutomataToken> decorated;

    public KeywordLexicalNode(LexicalNode<AutomataToken> source){
        decorated = source;
    }

    private AutomataToken applyMap(@Nullable AutomataToken sourceToken){
        if (sourceToken != null){
            TokenType keywordType = keywordMap.get(sourceToken.getLexeme().toString());
            if (keywordType != null){
                sourceToken.setType(keywordType);
            }
        }
        return sourceToken;
    }

    @Override
    public @Nullable AutomataToken process(@NotNull SourceCodeReader reader) throws LexicalException {
        return applyMap(decorated.process(reader));
    }

    @Override
    public void addBranch(@NotNull NodeBranch<AutomataToken> branch) {
        decorated.addBranch(branch);
    }

    @Override
    public void setName(String nodeName) {
        decorated.setName(nodeName);
    }

    @Override
    public String getName() {
        return decorated.getName();
    }

    public String toString(){
        return decorated.getName() + " (Keyword)";
    }

}
