package lexical.automata.omitter;

import lexical.automata.NodeBranchBuilder;
import lexical.automata.filter.LexicalFilter;

public class OmitterBranchBuilder implements NodeBranchBuilder<OmitterNode, OmitterNodeBuilder> {

    private final OmitterNodeBuilder omitterNodeBuilder;
    private final LexicalFilter filter;


    OmitterBranchBuilder(OmitterNodeBuilder omitterNodeBuilder, LexicalFilter filter) {
        this.omitterNodeBuilder = omitterNodeBuilder;
        this.filter = filter;
    }

    @Override
    public OmitterNodeBuilder thenRepeat() {
        omitterNodeBuilder.buildingNode.addBranch(new OmitterBranch(filter, omitterNodeBuilder.buildingNode));
        return omitterNodeBuilder;
    }

    @Override
    public OmitterNodeBuilder thenMoveTo(OmitterNode nextNode) {
        omitterNodeBuilder.buildingNode.addBranch(new OmitterBranch(filter, nextNode));
        return omitterNodeBuilder;
    }

    @Override
    public OmitterNodeBuilder thenTry(OmitterNode nextNode) {
        omitterNodeBuilder.buildingNode.addTryBranch(new TryOmitterBranch(filter, nextNode));
        return omitterNodeBuilder;
    }
}
