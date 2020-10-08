package lexical.automata.omitter;

import lexical.automata.LexicalNodeBuilder;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.omitter.strategy.OmitterNodeStrategy;
import org.jetbrains.annotations.NotNull;


public class OmitterNodeBuilder extends LexicalNodeBuilder<OmitterNode, OmitterBranchBuilder> {

    DefaultOmitterNode buildingNode;
    private final OmitterNodeStrategy strategy;

    public OmitterNodeBuilder(@NotNull OmitterNodeStrategy strategy) {
        this.strategy = strategy;
        buildingNode = new DefaultOmitterNode(strategy);
    }

    @Override
    protected OmitterBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new OmitterBranchBuilder(this, filter);
    }

    @Override
    public OmitterNode build(){
        OmitterNode builtNode = buildingNode;
        buildingNode = new DefaultOmitterNode(strategy);
        return builtNode;
    }


}
