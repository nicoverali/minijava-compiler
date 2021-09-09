package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;

public abstract class NodeBranchDecorator implements NodeBranch {

    protected NodeBranch decorated;

    public NodeBranchDecorator() {
        this.decorated = new ExceptionDecoratedBranch();
    }

    public NodeBranchDecorator(NodeBranch decorated) {
        this.decorated = decorated;
    }

    /**
     * Sets the branch that will be decorated with this decorator
     *
     * @param decorated a {@link NodeBranch} which will be decorated
     */
    public void setDecorated(NodeBranch decorated){
        this.decorated = decorated;
    }

    /**
     * {@inheritDoc}
     * <br><br>
     * The branch must have a decorated branch set, otherwise an exception will be thrown when trying to delegate
     * @param filter a {@link LexicalFilter} which will be used by this branch
     * @throws IllegalStateException if the branch does not have a decorated branch set
     */
    @Override
    public void setFilter(LexicalFilter filter) throws IllegalStateException{
        decorated.setFilter(filter);
    }

    /**
     * {@inheritDoc}
     * <br><br>
     * The branch must have a decorated branch set, otherwise an exception will be thrown when trying to delegate
     * @param nextNode the next {@link LexicalNode} of this branch
     * @throws IllegalStateException if the branch does not have a decorated branch set
     */
    @Override
    public void setNextNode(LexicalNode nextNode) throws IllegalStateException{
        decorated.setNextNode(nextNode);
    }

    @Override
    public boolean test(CodeCharacter character) {
        return decorated.test(character);
    }

    /**
     * {@inheritDoc}
     * <br><br>
     * The branch must have a decorated branch set, otherwise an exception will be thrown when trying to delegate
     *
     * @param reader a {@link SourceCodeReader} to take its next characters as input
     * @return an element of type <code>T</code> as a result of delegation
     * @throws LexicalException if a lexical error is detected
     * @throws IllegalStateException if the branch does not have a decorated branch set
     */
    @Override
    public abstract Token delegate(SourceCodeReader reader) throws LexicalException, IllegalStateException;

    /**
     * This is just a helper class to avoid having to check for null decorated branch.
     */
    private static class ExceptionDecoratedBranch implements NodeBranch{

        private static final String ERROR_MSG = "A decorator branch must have a decorated branch set before calling any method.";

        @Override
        public void setFilter(LexicalFilter filter) {
            throw new IllegalStateException(ERROR_MSG);
        }

        @Override
        public void setNextNode(LexicalNode nextNode) {
            throw new IllegalStateException(ERROR_MSG);
        }

        @Override
        public boolean test(CodeCharacter character) {
            throw new IllegalStateException(ERROR_MSG);
        }

        @Override
        public Token delegate(SourceCodeReader reader) throws LexicalException {
            throw new IllegalStateException(ERROR_MSG);
        }
    }
}
