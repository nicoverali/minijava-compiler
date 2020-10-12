package lexical.automata.node.builder;

import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.branch.DefaultNodeBranch;
import lexical.automata.branch.TryNodeBranch;
import lexical.automata.filter.*;

/**
 * @param <T> type of branch builder
 */
abstract class BaseLexicalNodeBuilder<T extends BaseLexicalNodeBuilder.BaseNodeBranchBuilder<?,?>> {

    abstract protected T createBranchBuilder(LexicalFilter filter);

    /**
     * Add a new branch to the node that will be selected if the character given to the node passes the lexical filter
     *
     * @param filter filter to test the characters given to the node
     * @return a nested builder to complete the branch
     */
    public T ifPass(LexicalFilter filter){
        return createBranchBuilder(filter);
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node is equal to the one
     * assigned to this branch.
     *
     * @param character character to test equality
     * @return a nested builder to complete the branch
     */
    public T ifCharEquals(char character){
        return createBranchBuilder(new CharacterEqualsFilter(character));
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node is not equal to the one
     * assigned to this branch.
     *
     * @param character character to test equality
     * @return a nested builder to complete the branch
     */
    public T ifCharNotEquals(char character){
        return createBranchBuilder(new CharacterNotEqualsFilter(character));
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node does not equal
     * any of the given exception characters.
     *
     * @param characters exception characters
     * @return a nested builder to complete the branch
     */
    public T ifAnyExcept(char... characters){
        return createBranchBuilder(new AnyCharacterExceptFilter(characters));
    }

    /**
     * Add a new branch to the node that will always be selected.
     *
     * @return a nested builder to complete the branch
     */
    public T ifAny() {
        return createBranchBuilder(new AnyCharacterFilter());
    }

    /**
     * @param <U> type of node builder
     * @param <S> type of element return by the next node
     */
    public abstract static class BaseNodeBranchBuilder<U extends BaseLexicalNodeBuilder<?>, S>{

        protected final U nodeBuilder;
        protected NodeBranch<S> buildingBranch;

        protected abstract LexicalNode<S> getBuildingNode();

        public BaseNodeBranchBuilder(LexicalFilter filter, U nodeBuilder){
            buildingBranch = new DefaultNodeBranch<>(filter);
            this.nodeBuilder = nodeBuilder;
        }

        /**
         * Sets the given {@link LexicalNode} as the next node of the branch being built.
         *
         * @param nextNode next {@link LexicalNode} of the branch being built
         * @return a node builder to keep building the node
         */
        public U thenMoveTo(LexicalNode<S> nextNode){
            buildingBranch.setNextNode(nextNode);
            getBuildingNode().addBranch(buildingBranch);
            return nodeBuilder;
        }

        /**
         * Sets the given {@link LexicalNode} as the next node of the {@link NodeBranch} being built.
         * The branch will try to delegate work to the node; if it can, then the result will be propagated
         * to the parent node, else a <code>null</code> value will be propagated.
         * The parent node then will be able select another branch in case this one fails.
         *
         * @see TryNodeBranch
         * @param nextNode next {@link LexicalNode} of this branch
         * @return a node builder to keep building the node
         */
        public U thenTry(LexicalNode<S> nextNode){
            buildingBranch.setNextNode(nextNode);
            getBuildingNode().addBranch(new TryNodeBranch<>(buildingBranch));
            return nodeBuilder;
        }

        /**
         * Sets the {@link LexicalNode} being built, as the next node
         * of the {@link NodeBranch} being built, creating a loop.
         *
         * @return a node builder to keep building the node
         */
        public U thenRepeat(){
            buildingBranch.setNextNode(getBuildingNode());
            getBuildingNode().addBranch(buildingBranch);
            return nodeBuilder;
        }



    }
}
