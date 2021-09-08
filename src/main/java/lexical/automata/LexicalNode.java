package lexical.automata;

import io.code.SourceCodeReader;
import lexical.LexicalException;

/**
 * A LexicalNode represents a single node in a lexical automata.
 * <br>
 * The full automata is obtain by connecting multiple LexicalNodes together with {@link NodeBranch}.
 * <br><br>
 * Every LexicalNode has a collection of {@link NodeBranch}, that connects it to other LexicalNodes, in order to
 * make some kind of processing of the characters of a source code.
 *
 * @param <T> type of element returned by this node
 */
public interface LexicalNode<T>{

    /**
     * Takes a {@link SourceCodeReader} as input and process its next character in order
     * to return an element of type <code>T</code>.
     * <br>
     * The node will use its associated {@link NodeBranch} to process the characters. Branches will be tested
     * in LIFO order, the first added branch will be tested first, then the second one and so on.
     * <br><br>
     * If the node can't process the next character, then it may simply return a <code>null</code> value, or
     * may throw a {@link LexicalException}, meaning that it detects a lexical error.
     *
     * @see #addBranch(NodeBranch)
     * @param reader a {@link SourceCodeReader} to take its next characters as input
     * @return an element of type <code>T</code> as a result of processing the next characters of <code>reader</code>,
     * or null if the node couldn't process the characters
     * @throws LexicalException if a lexical error is detected
     */
    T process(SourceCodeReader reader) throws LexicalException;

    /**
     * Adds a new {@link NodeBranch} to this node.
     * The order in which branches are added with this method will establish the order in which branches will
     * be tested by the node, that is to say, branches will be tested in LIFO order.
     *
     * @param branch a {@link NodeBranch} that connects this node with another one
     */
    void addBranch(NodeBranch<T> branch);

    /**
     * Sets a name for this node.
     * This is mainly for debugging purpose.
     *
     * @param nodeName a name for the node
     */
    void setName(String nodeName);

    /**
     * Returns the name assigned to the node or an empty String if it has no name.
     *
     * @return the name of the node or empty String if it has no name
     */
    String getName();

}
