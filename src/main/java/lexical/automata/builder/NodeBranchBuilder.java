package lexical.automata.builder;

import lexical.TokenType;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.branch.DefaultNodeBranch;
import lexical.automata.branch.filter.LexicalFilter;
import lexical.automata.node.BaseLexicalNode;
import lexical.automata.node.strategy.AcceptorStrategy;
import lexical.automata.node.strategy.LexicalNodeStrategy;
import lexical.automata.node.strategy.NonAcceptorStrategy;

public class NodeBranchBuilder{

    protected final LexicalFilter filter;
    protected final LexicalNodeBuilder nodeBuilder;

    NodeBranchBuilder(LexicalFilter filter, LexicalNodeBuilder nodeBuilder){
        this.filter = filter;
        this.nodeBuilder = nodeBuilder;
    }

    /**
     * Sets the given {@link LexicalNode} as the next node of the branch being built.
     *
     * @param nextNode next {@link LexicalNode} of the branch being built
     * @return a node builder to keep building the node
     */
    public LexicalNodeBuilder thenMoveTo(LexicalNode nextNode){
        nodeBuilder.addBranch(new DefaultNodeBranch(filter), nextNode);
        return nodeBuilder;
    }

    /**
     * Sets the given {@link LexicalNodeStrategy} as the strategy for the next node of the branch being built.
     *
     * @param strategy strategy for the next {@link LexicalNode} of the branch being built
     * @return a node builder to keep building the node
     */
    public LexicalNodeBuilder thenMoveToNodeWith(LexicalNodeStrategy strategy){
        nodeBuilder.addBranch(new DefaultNodeBranch(filter), new BaseLexicalNode("Node with strategy"+strategy, strategy));
        return nodeBuilder;
    }



    /**
     * Sets the {@link LexicalNode} being built, as the next node
     * of the {@link NodeBranch} being built, creating a loop.
     *
     * @return a node builder to keep building the node
     */
    public LexicalNodeBuilder thenRepeat(){
        nodeBuilder.addBranch(new DefaultNodeBranch(filter), null);
        return nodeBuilder;
    }

    /**
     * Makes the branch being built to move to a node that
     * throws a {@link lexical.LexicalException} with the given error message.
     *
     * @param errorMsg error message of the {@link lexical.LexicalException} thrown by the branch
     * @return a node builder to keep building the node
     */
    public LexicalNodeBuilder thenThrow(String errorMsg){
        nodeBuilder.addBranch(
                new DefaultNodeBranch(filter),
                new BaseLexicalNode("Non acceptor node", new NonAcceptorStrategy(errorMsg))
        );
        return nodeBuilder;
    }

    /**
     * Makes the branch being built to move to a node that
     * always accepts and returns a {@link lexical.Token}.
     *
     * @param tokenType type of the {@link lexical.Token} returned by the final node
     * @return a node builder to keep building the node
     */
    public LexicalNodeBuilder thenReturn(TokenType tokenType){
        nodeBuilder.addBranch(
                new DefaultNodeBranch(filter),
                new BaseLexicalNode("Acceptor node for token "+tokenType, new AcceptorStrategy(tokenType))
        );
        return nodeBuilder;
    }

    /**
     * Makes the branch being built to move to a node that
     * always accepts without returning any {@link lexical.Token}.
     *
     * @return a node builder to keep building the node
     */
    public LexicalNodeBuilder thenAccept(){
        nodeBuilder.addBranch(
                new DefaultNodeBranch(filter),
                new BaseLexicalNode("Acceptor node", new AcceptorStrategy())
        );
        return nodeBuilder;
    }

}
