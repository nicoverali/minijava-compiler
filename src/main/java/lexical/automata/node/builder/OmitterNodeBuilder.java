package lexical.automata.node.builder;

import lexical.TokenType;
import lexical.automata.AutomataLexeme;
import lexical.automata.AutomataToken;
import lexical.automata.LexicalNode;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.node.DefaultLexicalNode;
import lexical.automata.node.strategy.LexicalErrorStrategy;
import lexical.automata.node.strategy.ValueReturnStrategy;

public class OmitterNodeBuilder extends BaseLexicalNodeBuilder<OmitterNodeBuilder.OmitterBranchBuilder>{

    private final DefaultLexicalNode buildingNode;

    public OmitterNodeBuilder(String nodeName){
        buildingNode = new DefaultLexicalNode(nodeName);
    }

    @Override
    protected OmitterBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new OmitterBranchBuilder(filter, this);
    }

    /**
     * Makes the {@link LexicalNode} being built to accept the current state and thus return a new value
     * to its parent node.
     *
     * @return the final {@link LexicalNode}
     */
    public LexicalNode orElseAccept(){
        buildingNode.setStrategy(new ValueReturnStrategy(new AutomataToken(null, AutomataLexeme.empty())));
        return buildingNode;
    }

    /**
     * Makes the {@link LexicalNode} being built to reject the current state and return a <code>null</code> value,
     * meaning that it could not generate a result.
     *
     * @return the final {@link LexicalNode}
     */
    public LexicalNode orElseReject(){
        return buildingNode;
    }

    /**
     * Makes the {@link LexicalNode} being built to throw a {@link lexical.LexicalException} whenever it cannot
     * delegate the processing of characters
     *
     * @param msg the message of all {@link lexical.LexicalException} thrown by the {@link LexicalNode} being built
     * @return a node builder to keep building the node
     */
    public LexicalNode orElseThrow(String msg){
        buildingNode.setStrategy(new LexicalErrorStrategy(msg));
        return buildingNode;
    }

    public class OmitterBranchBuilder extends BaseLexicalNodeBuilder.BaseNodeBranchBuilder<OmitterNodeBuilder, OmitterBranchBuilder>{

        public OmitterBranchBuilder(LexicalFilter filter, OmitterNodeBuilder nodeBuilder) {
            super(filter, nodeBuilder);
        }

        @Override
        protected OmitterBranchBuilder getThis() {
            return this;
        }

        @Override
        protected LexicalNode getBuildingNode() {
            return buildingNode;
        }
    }

}
