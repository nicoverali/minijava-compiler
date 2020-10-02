package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface OmitterNodeStrategy {

    /**
     * Determines what to do in case no branch can match a certain character.
     * <br>
     * If the node detects a lexical error, then a {@link LexicalException} will be thrown.
     *
     * @param currentCharacter current {@link CodeCharacter}
     * @throws LexicalException if the node detects a lexical error
     */
    void onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException;

    /**
     * Determines what to do in case the node reaches the end of file.
     *
     * @param currentLine last line of the file, or null if its empty
     * @throws LexicalException if the node detects a lexical error
     */
    void onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException;

}
