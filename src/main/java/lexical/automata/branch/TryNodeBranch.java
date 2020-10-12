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
public class TryNodeBranch<T> implements NodeBranch<T> {

    private static final int READ_AHEAD_LIMIT = 20;
    private final NodeBranch<T> decorated;

    public TryNodeBranch(NodeBranch<T> decorated) {
        this.decorated = decorated;
    }

    @Override
    public void setFilter(LexicalFilter filter) {
        decorated.setFilter(filter);
    }

    @Override
    public void setNextNode(LexicalNode<T> nextNode) {
        decorated.setNextNode(nextNode);
    }

    /**
     * As any other {@link NodeBranch}, it will test the next character of the {@link SourceCodeReader} and if it
     * passes the filter, then the branch will delegate processing to the next {@link LexicalNode}.
     * <br>
     * This branch in particular, will restore the state of the <code>reader</code> if the next node throws a
     * {@link LexicalException} because it detected a lexical error, besides the branch won't propagate the exception
     * and instead will simply return a <code>null</code> value as if the next node could not process the character.
     *
     * @see #setFilter(LexicalFilter)
     * @param reader a {@link SourceCodeReader} to take its next characters as input
     * @return an element of type <code>T</code> returned by the next node, or null if the next node couldn't process
     * the next character
     */
    @Override
    public @Nullable T delegate(SourceCodeReader reader) {
        reader.mark(READ_AHEAD_LIMIT);
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
