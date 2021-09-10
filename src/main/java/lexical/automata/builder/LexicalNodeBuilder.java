package lexical.automata.builder;

import lexical.Token;
import lexical.TokenType;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.branch.filter.*;
import lexical.automata.node.BaseLexicalNode;
import lexical.automata.node.strategy.AcceptorStrategy;
import lexical.automata.node.strategy.LexicalNodeStrategy;
import lexical.automata.node.strategy.NonAcceptorStrategy;
import util.Pair;

import java.util.ArrayList;
import java.util.List;

public class LexicalNodeBuilder {

    private final List<Pair<NodeBranch, LexicalNode>> branches = new ArrayList<>();
    private final String nodeName;

    public LexicalNodeBuilder(String nodeName) {
        this.nodeName = nodeName;
    }

    void addBranch(NodeBranch branch, LexicalNode nextNode){
        branches.add(Pair.of(branch, nextNode));
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node passes the lexical filter
     *
     * @param filter filter to test the characters given to the node
     * @return a nested builder to complete the branch
     */
    public NodeBranchBuilder ifPass(LexicalFilter filter){
        return new NodeBranchBuilder(filter, this);
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node is equal to the one
     * assigned to this branch.
     *
     * @param character character to test equality
     * @return a nested builder to complete the branch
     */
    public NodeBranchBuilder ifEquals(char character){
        return new NodeBranchBuilder(new CharacterEqualsFilter(character), this);
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node is not equal to the one
     * assigned to this branch.
     *
     * @param character character to test equality
     * @return a nested builder to complete the branch
     */
    public NodeBranchBuilder ifCharNotEquals(char character){
        return new NodeBranchBuilder(new CharacterNotEqualsFilter(character), this);
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node does not equal
     * any of the given exception characters.
     *
     * @param characters exception characters
     * @return a nested builder to complete the branch
     */
    public NodeBranchBuilder ifAnyExcept(char... characters){
        return new NodeBranchBuilder(new AnyCharacterExceptFilter(characters), this);
    }

    /**
     * Add a new branch to the node that will always be selected.
     *
     * @return a nested builder to complete the branch
     */
    public NodeBranchBuilder ifAny() {
        return new NodeBranchBuilder(new AnyCharacterFilter(), this);
    }

    /**
     * Makes the {@link LexicalNode} being built to accept the current state and thus return a new value
     * to its parent node.
     *
     * @return the final {@link LexicalNode}
     */
    public LexicalNode orElseAccept(){
        LexicalNode node = new BaseLexicalNode(nodeName, new AcceptorStrategy());
        addBranchesToNode(node, branches);
        return node;
    }

    /**
     * Makes the {@link LexicalNode} being built return a {@link Token} of the specified type whenever it cannot
     * delegate the processing of characters.
     *
     * @param type {@link TokenType} of tokens returned by the {@link LexicalNode} being built
     * @return the final built {@link LexicalNode}
     */
    public LexicalNode orElseReturn(TokenType type){
        LexicalNode node = new BaseLexicalNode(nodeName, new AcceptorStrategy(type));
        addBranchesToNode(node, branches);
        return node;
    }

    /**
     * Makes the {@link LexicalNode} being built to throw a {@link lexical.LexicalException} whenever it cannot
     * delegate the processing of characters
     *
     * @param msg the message of all {@link lexical.LexicalException} thrown by the {@link LexicalNode} being built
     * @return the final built {@link LexicalNode}
     */
    public LexicalNode orElseThrow(String msg){
        LexicalNode node = new BaseLexicalNode(nodeName, new NonAcceptorStrategy(msg));
        addBranchesToNode(node, branches);
        return node;
    }

    /**
     * Makes the {@link LexicalNode} being built to run the given strategy whenever it cannot
     * delegate the processing of characters
     *
     * @param strategy the strategy the node will take if it cannot delegate the processing of characters
     * @return the final built {@link LexicalNode}
     */
    public LexicalNode orElse(LexicalNodeStrategy strategy){
        LexicalNode node = new BaseLexicalNode(nodeName, strategy);
        addBranchesToNode(node, branches);
        return node;
    }

    private void addBranchesToNode(LexicalNode node, List<Pair<NodeBranch, LexicalNode>> branches) {
        for (Pair<NodeBranch, LexicalNode> pair : branches){
            LexicalNode nextNode = pair.right != null
                    ? pair.right
                    : node;

            pair.left.setNextNode(nextNode);
            node.addBranch(pair.left);
        }
    }


}
