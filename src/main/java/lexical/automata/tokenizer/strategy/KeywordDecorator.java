package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    private TokenizerNodeStrategy source;

    public KeywordDecorator(TokenizerNodeStrategy source){
        this.source = source;
    }

    @Override
    public Optional<Token> onNoBranchSelected(@NotNull Lexeme lexeme, @NotNull CodeCharacter currentCharacter) throws LexicalException {
        return source.onNoBranchSelected(lexeme, currentCharacter)
                .map(this::applyMap);
    }

    @Override
    public Optional<Token> onEndOfFile(@NotNull Lexeme lexeme, @Nullable CodeLine currentLine) throws LexicalException {
        return source.onEndOfFile(lexeme, currentLine)
                .map(this::applyMap);
    }

    private Token applyMap(Token sourceToken){
        TokenType keywordType = keywordMap.get(sourceToken.getLexeme());
        if (keywordType != null){
            sourceToken.setType(keywordType);
        }
        return sourceToken;
    }
}
