package lexical.automata.omitter;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.DelegateNodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.NotNull;

public class OmitterBranch implements DelegateNodeBranch<OmitterNode, Void> {

    private final LexicalFilter filter;
    private final OmitterNode nextNode;

    public OmitterBranch(LexicalFilter filter, OmitterNode nextNode) {
        this.filter = filter;
        this.nextNode = nextNode;
    }

    @Override
    public @NotNull LexicalFilter getFilter() {
        return filter;
    }

    public @NotNull OmitterNode getNextNode() {
        return nextNode;
    }

    @Override
    public @NotNull Void delegate(SourceCodeReader reader) throws LexicalException {
        reader.next(); // Consume character
        nextNode.omit(reader);
        return null;
    }
}
