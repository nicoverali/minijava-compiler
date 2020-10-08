package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OmitterMiddleStrategy implements OmitterNodeStrategy {

    private final String errorMsg;

    /**
     * Creates a new {@link OmitterMiddleStrategy} that generates {@link LexicalException} with the <code>errorMsg</code>
     * given as argument.
     *
     * @param errorMsg error message to add on all {@link LexicalException} thrown by this node
     */
    public OmitterMiddleStrategy(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public void onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
        CodeLine line = currentCharacter.getCodeLine();
        Lexeme lexeme = Lexeme.empty(line.getLineNumber());
        throw new LexicalException(errorMsg, lexeme, line.toString(), line.getLineNumber(), currentCharacter.getColumnNumber());
    }

    @Override
    public void onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        // Only if file is not empty we detect a lexical error
        if (currentLine != null){
            int lineNumber = currentLine.getLineNumber();
            throw new LexicalException(errorMsg, Lexeme.empty(lineNumber), currentLine.toString(), lineNumber, currentLine.getSize());
        }
    }

}
