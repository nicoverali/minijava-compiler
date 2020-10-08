package lexical.automata;

/**
 * This builder is complements the {@link LexicalNodeBuilder}. Once the latter defines a filter to be used on a new
 * {@link NodeBranch}, it returns this builder to define the other aspects of the branch.
 *
 * @param <T> type of {@link LexicalNode}
 * @param <B> type of {@link LexicalNodeBuilder}
 */
public interface NodeBranchBuilder<T extends LexicalNode<?,?>, B extends LexicalNodeBuilder<T, ?>> {

    /**
     * Sets the next node of this branch to the same node, creating a loop.
     *
     * @return the {@link LexicalNodeBuilder} to keep building the node
     */
    B thenRepeat();

    /**
     * Sets the given {@link LexicalNode} as the next node of this branch.
     *
     * @param nextNode next {@link LexicalNode} of this branch
     * @return the {@link LexicalNodeBuilder} to keep building the node
     */
    B thenMoveTo(T nextNode);

    /**
     * Sets the given {@link LexicalNode} as the next node if this branch.
     * The branch will try to delegate work to the node; if it can, then the result will be propagated
     * to the parent node, else a <code>null</code> value will be propagated.
     * The parent node then will be able select another branch in case this one fails.
     *
     * @see TryNodeBranch
     * @param nextNode next {@link LexicalNode} of this branch
     * @return the {@link LexicalNodeBuilder} to keep building the node
     */
    B thenTry(T nextNode);

}
