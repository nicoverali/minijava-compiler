package lexical.automata.omitter;

import lexical.automata.LexicalNodeBuilder;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.omitter.strategy.OmitterNodeStrategy;
import org.jetbrains.annotations.NotNull;


public class OmitterNodeBuilder extends LexicalNodeBuilder<OmitterNode, OmitterBranchBuilder> {

    OmitterNode buildingNode;
    private final OmitterNodeStrategy strategy;

    public OmitterNodeBuilder(@NotNull OmitterNodeStrategy strategy) {
        this.strategy = strategy;
        buildingNode = new OmitterNode(strategy, new NodeBranchContainer<>());
    }

    @Override
    protected OmitterBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new OmitterBranchBuilder(this, filter);
    }

    @Override
    public OmitterNode build(){
        OmitterNode builtNode = buildingNode;
        buildingNode = new OmitterNode(strategy, new NodeBranchContainer<>());
        return builtNode;
    }


}
