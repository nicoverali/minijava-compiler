package lexical.automata.node.builder;

import lexical.automata.LexicalNode;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.node.DefaultLexicalNode;
import lexical.automata.node.strategy.LexicalErrorStrategy;

public class OmitterNodeBuilder extends BaseLexicalNodeBuilder<OmitterNodeBuilder.OmitterBranchBuilder>{

    private final DefaultLexicalNode<Void> buildingNode;

    public OmitterNodeBuilder(String nodeName){
        buildingNode = new DefaultLexicalNode<>(nodeName);
    }

    @Override
    protected OmitterBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new OmitterBranchBuilder(filter, this);
    }

    /**
     * Makes the {@link LexicalNode} being built to simply return whenever it cannot
     * delegate the processing of characters
     *
     * @return a node builder to keep building the node
     */
    public LexicalNode<Void> orElseReturn(){
        return buildingNode;
    }

    /**
     * Makes the {@link LexicalNode} being built to throw a {@link lexical.LexicalException} whenever it cannot
     * delegate the processing of characters
     *
     * @param msg the message of all {@link lexical.LexicalException} thrown by the {@link LexicalNode} being built
     * @return a node builder to keep building the node
     */
    public LexicalNode<Void> orElseThrow(String msg){
        buildingNode.setStrategy(new LexicalErrorStrategy<>(msg));
        return buildingNode;
    }

    public class OmitterBranchBuilder extends BaseLexicalNodeBuilder.BaseNodeBranchBuilder<OmitterNodeBuilder, Void>{

        public OmitterBranchBuilder(LexicalFilter filter, OmitterNodeBuilder nodeBuilder) {
            super(filter, nodeBuilder);
        }

        @Override
        protected LexicalNode<Void> getBuildingNode() {
            return buildingNode;
        }
    }

}
