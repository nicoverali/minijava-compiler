package lexical.automata.branch;

import io.code.SourceCodeReader;
import lexical.Token;
import lexical.automata.NodeBranch;


/**
 * This type of {@link NodeBranch} delegates processing of characters as usual, but only consumes the
 * current character if the delegation does actually produce a result, or put another way, if the characters
 * are accepted by the following nodes
 */
public class TryNodeBranch extends NodeBranchDecorator {

    private final int readAheadLimit;

    public TryNodeBranch(NodeBranch decorated, int aheadLimit) {
        super(decorated);
        readAheadLimit = aheadLimit;
    }

    @Override
    public Token delegate(SourceCodeReader reader) {
        reader.mark(readAheadLimit);
        Token result = decorated.delegate(reader);
        if (rejects(result)) reader.reset();
        return result;
    }

    private boolean rejects(Token result){
        return result == null;
    }

    @Override
    public String toString(){
        return "Try: " + decorated;
    }
}
