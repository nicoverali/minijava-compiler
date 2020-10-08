package lexical.automata;

import lexical.automata.filter.*;

/**
 * @param <T> type of lexical node
 * @param <S> type of branch builder
 */
public abstract class LexicalNodeBuilder<T extends LexicalNode<?,?>, S extends NodeBranchBuilder<T, ?>> {

    abstract protected S createBranchBuilder(LexicalFilter filter);

    /**
     * Add a new branch to the node that will be selected if the character given to the node passes the lexical filter
     *
     * @param filter filter to test the characters given to the node
     * @return a nested builder to complete the branch
     */
    public S ifPass(LexicalFilter filter){
        return createBranchBuilder(filter);
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node is equal to the one
     * assigned to this branch.
     *
     * @param character character to test equality
     * @return a nested builder to complete the branch
     */
    public S ifCharEquals(char character){
        return createBranchBuilder(new CharacterEqualsFilter(character));
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node is not equal to the one
     * assigned to this branch.
     *
     * @param character character to test equality
     * @return a nested builder to complete the branch
     */
    public S ifCharNotEquals(char character){
        return createBranchBuilder(new CharacterNotEqualsFilter(character));
    }

    /**
     * Add a new branch to the node that will be selected if the character given to the node does not equal
     * any of the given exception characters.
     *
     * @param characters exception characters
     * @return a nested builder to complete the branch
     */
    public S ifAnyExcept(char... characters){
        return createBranchBuilder(new AnyCharacterExceptFilter(characters));
    }

    /**
     * Add a new branch to the node that will always be selected.
     *
     * @return a nested builder to complete the branch
     */
    public S ifAny() {
        return createBranchBuilder(new AnyCharacterFilter());
    }

    /**
     * Builds a new {@link LexicalNode} with all the branches added to this builder
     *
     * @return a new {@link LexicalNode}
     */
    abstract public T build();


}
