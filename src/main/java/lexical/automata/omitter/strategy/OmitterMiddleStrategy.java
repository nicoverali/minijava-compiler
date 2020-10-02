package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OmitterMiddleStrategy implements OmitterNodeStrategy {

    private String errorMsg;

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
        throw new LexicalException(errorMsg, currentCharacter.getColumnNumber(), currentCharacter.getCodeLine());
    }

    @Override
    public void onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        // Only if file is not empty we detect a lexical error
        if (currentLine != null){
            throw new LexicalException(errorMsg, currentLine.getSize(), currentLine);
        }
    }

}
