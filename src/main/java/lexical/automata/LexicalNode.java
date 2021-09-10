package lexical.automata;

import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;

/**
 * A LexicalNode represents a single node in a lexical automata.
 * <br>
 * The full automata is obtained by connecting multiple LexicalNodes together with {@link NodeBranch}.
 * <br><br>
 * Every LexicalNode has a collection of {@link NodeBranch}, that connects it to other LexicalNodes, in order to
 * make some kind of processing of the characters of a source code.
 * <br>
 * Each node can be an acceptor or non-acceptor. Acceptor nodes will return without errors, they may or may not
 * return a not-null {@link Token}. Non-acceptor nodes will fail if there's no branch that can delegate
 * the next character.
 */
public interface LexicalNode{

    /**
     * Takes a {@link SourceCodeReader} as input and process its next character in order
     * to return a {@link Token}.
     * <br>
     * The node will use its associated {@link NodeBranch} to process the characters. Branches will be tested
     * in FIFO order, the first added branch will be tested first, then the second one and so on.
     * <br><br>
     * If the node is an acceptor then it will return a nullable {@link Token} in case no branch matches, else,
     * if the node is non-acceptor it will throw a {@link LexicalException} representing a Lexical error
     *
     * @see #addBranch(NodeBranch)
     * @param reader a {@link SourceCodeReader} to take its next characters as input
     * @param currentLexeme the current lexeme
     * @return a nullable {@link Token}
     * @throws LexicalException if a lexical error is detected
     */
    Token process(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException;

    /**
     * Adds a new {@link NodeBranch} to this node.
     * The order in which branches are added with this method will establish the order in which branches will
     * be tested by the node, that is to say, branches will be tested in FIFO order.
     *
     * @param branch a {@link NodeBranch} that connects this node with another one
     */
    void addBranch(NodeBranch branch);

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
