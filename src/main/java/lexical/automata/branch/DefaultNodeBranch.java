package lexical.automata.branch;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.Nullable;

public class DefaultNodeBranch<T> implements NodeBranch<T> {

    private LexicalFilter filter;
    private LexicalNode<T> nextNode;

    public DefaultNodeBranch(LexicalFilter filter){
        this.filter = filter;
    }

    @Override
    public void setFilter(LexicalFilter filter) {
        this.filter = filter;
    }

    @Override
    public void setNextNode(LexicalNode<T> nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public @Nullable T delegate(SourceCodeReader reader) throws LexicalException {
        if (nextNode != null && shouldDelegate(reader)){
            reader.next(); // Consume character
            return nextNode.process(reader);
        } else {
            return null;
        }
    }

    private boolean shouldDelegate(SourceCodeReader reader){
        return reader.peek()
                .map(character -> filter.test(character.getValue()))
                .orElse(false);
    }
}
