package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OmitterEndStrategy implements OmitterNodeStrategy {

    @Override
    public void onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
    }

    @Override
    public void onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
    }
}
