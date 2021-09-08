package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.node.strategy.NullStrategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * This {@link LexicalNode} will try to delegate the processing of {@link SourceCodeReader} characters to another
 * node.
 * <br>
 * It has a {@link LexicalNodeStrategy} to determine what to do if the processing of characters cannot be delegated.
 *
 * @param <T> type of element return by the node
 */
public class DefaultLexicalNode<T> implements LexicalNode<T> {

    private final Deque<NodeBranch<T>> branches = new ArrayDeque<>();

    private LexicalNodeStrategy<T> strategy = new NullStrategy<>();
    private String name;

    public DefaultLexicalNode(String name) {
        this.name = name;
    }

    public DefaultLexicalNode(String name, LexicalNodeStrategy<T> strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public void setStrategy(LexicalNodeStrategy<T> strategy){
        this.strategy = strategy;
    }

    @Override
    public void addBranch(NodeBranch<T> branch) {
        branches.add(branch);
    }


    @Override
    public T process(SourceCodeReader reader) throws LexicalException {
        Optional<CodeCharacter> nextChar = reader.peek();
        if (nextChar.isEmpty()){
            return strategy.onEndOfFile(reader, reader.getCurrentLine().orElse(null));
        }

        for (NodeBranch<T> branch : branches){
            T result = branch.delegate(reader);
            if (result != null){
                return result;
            }
        }

        return strategy.onNoBranchSelected(reader, nextChar.get());
    }

    @Override
    public void setName(String nodeName) {
        this.name = nodeName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
