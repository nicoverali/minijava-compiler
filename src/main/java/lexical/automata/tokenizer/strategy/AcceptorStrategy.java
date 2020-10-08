package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AcceptorStrategy implements TokenizerNodeStrategy{

    private final TokenType type;

    /**
     * Creates a new acceptor strategy that will generate tokens with the given {@link TokenType}
     *
     * @param type type of {@link Token} to generate when accepting
     */
    public AcceptorStrategy(TokenType type) {
        this.type = type;
    }

    @Override
    public @NotNull Token onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
        return new Token(type, Lexeme.empty(currentCharacter.getLineNumber()));
    }

    @Override
    public @NotNull Token onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        if (currentLine != null){
            return new Token(type, Lexeme.empty(currentLine.getLineNumber()));
        }
        return new Token(type, Lexeme.empty(0));
    }
}
