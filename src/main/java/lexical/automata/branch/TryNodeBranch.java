package lexical.automata.branch;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.Nullable;

/**
 * This branch works like a common branch, but when it detects a lexical error,
 * it will not propagate the exceptions and will only return a <code>null</code> value, besides in that case
 * it will not consume any character of the {@link SourceCodeReader}
 *
 * @param <T> type of element return by the {@link LexicalNode} it connects
 */
public class TryNodeBranch<T> extends NodeBranchDecorator<T> {

    private final int readAheadLimit;

    public TryNodeBranch(NodeBranch<T> decorated, int aheadLimit) {
        super(decorated);
        readAheadLimit = aheadLimit;
    }

    @Override
    public @Nullable T delegate(SourceCodeReader reader) {
        reader.mark(readAheadLimit);
        try {
            return decorated.delegate(reader);
        } catch (LexicalException e){
            reader.reset();
            return null;
        }
    }

    @Override
    public String toString(){
        return "Try: " + decorated;
    }
}
