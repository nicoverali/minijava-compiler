package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.node.LexicalNodeStrategy;
import lexical.automata.AutomataLexeme;
import lexical.automata.AutomataToken;

/**
 * This {@link LexicalNodeStrategy} generates a new {@link Token} when the node cannot delegate.
 */
public class TokenizeStrategy implements LexicalNodeStrategy {

    private final TokenType type;

    public TokenizeStrategy(TokenType type) {
        this.type = type;
    }

    @Override
    public  AutomataToken onNoBranchSelected(SourceCodeReader reader,  CodeCharacter currentCharacter) throws LexicalException {
        return new AutomataToken(type, AutomataLexeme.empty(currentCharacter.getCodeLine()));
    }

    @Override
    public  AutomataToken onEndOfFile(SourceCodeReader reader,  CodeLine currentLine) throws LexicalException {
        return new AutomataToken(type, AutomataLexeme.empty(currentLine));
    }
}
