package lexical.automata.branch;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.Nullable;


/**
 * This type of {@link NodeBranch} delegates processing of characters as usual, but only consumes the
 * current character if the delegation does actually produce a result, or put another way, if the characters
 * are accepted by the following nodes
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
        T result = decorated.delegate(reader);
        if (rejects(result)) reader.reset();
        return result;
    }

    private boolean rejects(T result){
        return result == null;
    }

    @Override
    public String toString(){
        return "Try: " + decorated;
    }
}
