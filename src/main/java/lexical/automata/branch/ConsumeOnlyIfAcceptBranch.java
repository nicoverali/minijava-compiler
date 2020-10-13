package lexical.automata.branch;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.NodeBranch;
import org.jetbrains.annotations.Nullable;

/**
 * This type of {@link NodeBranch} delegates processing of characters as usual, but only consumes the
 * current character if the delegation does actually produce a result, or put another way, if the characters
 * are accepted by the following nodes
 */
public class ConsumeOnlyIfAcceptBranch<T> extends NodeBranchDecorator<T> {

    private final int aheadLimit;

    public ConsumeOnlyIfAcceptBranch(NodeBranch<T> decorated, int aheadLimit) {
        super(decorated);
        this.aheadLimit = aheadLimit;
    }

    @Override
    public @Nullable T delegate(SourceCodeReader reader) throws LexicalException {
        reader.mark(aheadLimit);
        T result = decorated.delegate(reader);
        if (!didAccept(result)) reader.reset();
        return result;
    }

    private boolean didAccept(T result){
        return result != null;
    }
}
