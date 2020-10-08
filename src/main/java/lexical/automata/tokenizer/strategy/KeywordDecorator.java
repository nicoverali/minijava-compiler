package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static lexical.TokenType.*;

public class KeywordDecorator implements TokenizerNodeStrategy{

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

    private final TokenizerNodeStrategy source;

    public KeywordDecorator(TokenizerNodeStrategy source){
        this.source = source;
    }



    private Token applyMap(@Nullable Token sourceToken){
        if (sourceToken != null){
            TokenType keywordType = keywordMap.get(sourceToken.getLexeme().toString());
            if (keywordType != null){
                sourceToken.setType(keywordType);
            }
        }
        return sourceToken;
    }

    @Override
    public @NotNull Token onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
        Token sourceToken = source.onNoBranchSelected(currentCharacter);
        return applyMap(sourceToken);
    }

    @Override
    public @NotNull Token onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        Token sourceToken = source.onEndOfFile(currentLine);
        return applyMap(sourceToken);
    }
}
