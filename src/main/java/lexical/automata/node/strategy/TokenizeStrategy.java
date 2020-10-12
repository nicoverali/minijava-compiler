package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.node.LexicalNodeStrategy;
import lexical.automata.AutomataLexeme;
import lexical.automata.AutomataToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This {@link LexicalNodeStrategy} generates a new {@link Token} when the node cannot delegate.
 */
public class TokenizeStrategy implements LexicalNodeStrategy<AutomataToken> {

    private final TokenType type;

    public TokenizeStrategy(TokenType type) {
        this.type = type;
    }

    @Override
    public @NotNull AutomataToken onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
        return new AutomataToken(type, AutomataLexeme.empty(currentCharacter.getCodeLine()));
    }

    @Override
    public @NotNull AutomataToken onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        return new AutomataToken(type, AutomataLexeme.empty(currentLine));
    }
}
