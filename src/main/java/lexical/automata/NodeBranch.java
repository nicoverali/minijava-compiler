package lexical.automata;

import lexical.automata.filter.LexicalFilter;

public interface NodeBranch<T extends LexicalNode<?>> {

    /**
     * Returns the {@link LexicalFilter} of this branch. A branch should be selected only if its associated filter
     * allows it.
     *
     * @return the {@link LexicalFilter} associated with this branch
     */
    LexicalFilter getFilter();

    /**
     * A branch at its end has another {@link LexicalNode} to which work should be delegated if the branch is selected.
     *
     * @return the {@link LexicalNode} at the end of this branch
     */
    T getNextNode();

}
