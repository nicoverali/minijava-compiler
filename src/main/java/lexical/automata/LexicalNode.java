package lexical.automata;

/**
 * A LexicalNode represents a single node in a lexical automata.
 * <br>
 * The full automata is obtain by connecting multiple LexicalNodes together with {@link NodeBranch}s.
 * <br><br>
 * Every LexicalNode has a collection of {@link NodeBranch}, that connects it to other LexicalNodes, in order to
 * make some kind of processing of the characters of a source code.
 *
 * @param <T> type of delegate branches
 * @param <U> type of try branches
 */
public interface LexicalNode<T extends DelegateNodeBranch<?,?>, U extends TryNodeBranch<?,?>> {

    /**
     * Adds a new {@link DelegateNodeBranch} to this node.
     *
     * @param branch a {@link DelegateNodeBranch} that connects this node with another one
     */
    void addBranch(T branch);

    /**
     * Similar to {@link #addBranch}, adds a new {@link TryNodeBranch} to this node, but if delegating
     * in any of this branches causes an exception, then it will be skipped and another branch will be selected.
     * <br><br>
     * You can assume that all try-branches will be tested first, before common branches.
     *
     * @param branch a {@link TryNodeBranch} that connects this node with another one
     */
    void addTryBranch(U branch);

}
