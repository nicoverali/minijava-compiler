package lexical.automata;

import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.NotNull;

public interface NodeBranch<T extends LexicalNode<?,?>> {

    /**
     * Returns the {@link LexicalFilter} of this branch. A branch should be selected only if its associated filter
     * allows it.
     *
     * @return the {@link LexicalFilter} associated with this branch
     */
    @NotNull
    LexicalFilter getFilter();

}
