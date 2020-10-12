package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import lexical.automata.node.LexicalNodeStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This {@link LexicalNodeStrategy} does not do anything if the node cannot delegate, and simply returns a
 * <code>null</code> value.
 *
 * @param <T> type of element returned by the {@link lexical.automata.LexicalNode}
 */
public class NullStrategy<T> implements LexicalNodeStrategy<T> {

    @Override
    public @Nullable T onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
        return null;
    }

    @Override
    public @Nullable T onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        return null;
    }

}
