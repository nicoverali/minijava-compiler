package lexical.automata.omitter;

import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;

public class OmitterBranch implements NodeBranch<OmitterNode> {

    private final LexicalFilter filter;
    private final OmitterNode nextNode;

    public OmitterBranch(LexicalFilter filter, OmitterNode nextNode) {
        this.filter = filter;
        this.nextNode = nextNode;
    }

    @Override
    public LexicalFilter getFilter() {
        return filter;
    }

    @Override
    public OmitterNode getNextNode() {
        return nextNode;
    }
}
