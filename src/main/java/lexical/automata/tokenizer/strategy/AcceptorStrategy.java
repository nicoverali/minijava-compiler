package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AcceptorStrategy implements TokenizerNodeStrategy{

    private TokenType type;

    /**
     * Creates a new acceptor strategy that will generate tokens with the given {@link TokenType}
     *
     * @param type type of {@link Token} to generate when accepting
     */
    public AcceptorStrategy(TokenType type) {
        this.type = type;
    }

    @Override
    public Optional<Token> onNoBranchSelected(@NotNull Lexeme lexeme, @NotNull CodeCharacter currentCharacter) throws LexicalException {
        return Optional.of(new Token(type, lexeme.getLexeme(), lexeme.getLineNumber()));
    }

    @Override
    public Optional<Token> onEndOfFile(@Nullable Lexeme lexeme, @Nullable CodeLine currentLine) throws LexicalException {
        if (lexeme != null){
            return Optional.of(new Token(type, lexeme.getLexeme(), lexeme.getLineNumber()));
        } else if (currentLine != null){
            return Optional.of(new Token(type, "", currentLine.getLineNumber()));
        } else {
            return Optional.of(new Token(type, "", 0));
        }
    }

}
