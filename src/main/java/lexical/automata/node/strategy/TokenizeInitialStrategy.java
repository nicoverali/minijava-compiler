package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.TokenType;
import lexical.automata.AutomataLexeme;
import lexical.automata.AutomataToken;
import lexical.automata.node.LexicalNodeStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * This strategy represents the strategy of the initial node of a tokenize automata.
 * It throws an exception if a character cannot be process, and returns the EOF token when the end of file gets reached.
 */
public class TokenizeInitialStrategy implements LexicalNodeStrategy<AutomataToken> {

    private final String errorMsg;

    public TokenizeInitialStrategy(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public @Nullable AutomataToken onNoBranchSelected(SourceCodeReader reader, @NotNull CodeCharacter currentCharacter) throws LexicalException {
        reader.next(); // Consume character
        throw new LexicalException(errorMsg, AutomataLexeme.empty(currentCharacter.getCodeLine()));
    }

    @Override
    public @Nullable AutomataToken onEndOfFile(SourceCodeReader reader, @Nullable CodeLine currentLine) throws LexicalException {
        return new AutomataToken(TokenType.EOF, AutomataLexeme.empty(currentLine));
    }
}
