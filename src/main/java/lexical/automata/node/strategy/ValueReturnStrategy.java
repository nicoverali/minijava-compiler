package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.node.LexicalNodeStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This strategy will always return a certain value if the node cannot delegate or the end of file has been reached.
 *
 * @param <T> type of value returned by this strategy
 */
public class ValueReturnStrategy<T> implements LexicalNodeStrategy<T> {

    private final T value;

    public ValueReturnStrategy(T value){
        this.value = value;
    }

    @Override
    public @Nullable T onNoBranchSelected(SourceCodeReader reader, @NotNull CodeCharacter currentCharacter) throws LexicalException {
        return value;
    }

    @Override
    public @Nullable T onEndOfFile(SourceCodeReader reader, @Nullable CodeLine currentLine) throws LexicalException {
        return value;
    }

}
