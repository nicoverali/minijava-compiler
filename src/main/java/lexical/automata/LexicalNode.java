package lexical.automata;

/**
 * A LexicalNode represents a single node in a lexical automata.
 * <br>
 * The full automata is obtain by connecting multiple LexicalNodes together with {@link NodeBranch}s.
 * <br><br>
 * Every LexicalNode has a collection of {@link NodeBranch}, that connects it to other LexicalNodes, in order to
 * make some kind of processing of the characters of a source code.
 *
 * @param <T> type of branches
 */
public interface LexicalNode<T extends NodeBranch<?>> {

    /**
     * Adds a new {@link NodeBranch} to this node.
     *
     * @param branch a {@link NodeBranch} that connects this node with another one
     */
    void addBranch(T branch);

}
