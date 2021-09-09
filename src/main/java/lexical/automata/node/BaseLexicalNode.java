package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.node.strategy.NullStrategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public abstract class BaseLexicalNode implements LexicalNode {

    private final Deque<NodeBranch> branches = new ArrayDeque<>();

    private LexicalNodeStrategy strategy = new NullStrategy();
    private String name;

    public BaseLexicalNode(String name) {
        this.name = name;
    }

    public void setStrategy(LexicalNodeStrategy strategy){
        this.strategy = strategy;
    }

    @Override
    public void addBranch(NodeBranch branch) {
        branches.add(branch);
    }


    @Override
    public Token process(SourceCodeReader reader) throws LexicalException {
        Optional<CodeCharacter> nextChar = reader.peek();

        if (nextChar.isPresent()){
            for (NodeBranch branch : branches){
                Token result = branch.delegate(reader);
                if (result != null){
                    return result;
                }
            }
        }

        return onNoBranchSelected(
                reader.getCurrentLine().orElse(null),
                nextChar.orElse(null)
        );
    }

    /**
     * Decides what to return or do if no branch could be selected
     */
    abstract protected Token onNoBranchSelected(CodeLine currentLine, CodeCharacter nextChar);

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
