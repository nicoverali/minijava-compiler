package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.Lexeme;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.node.strategy.LexicalNodeStrategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class BaseLexicalNode implements LexicalNode {

    private final Deque<NodeBranch> branches = new ArrayDeque<>();
    private final LexicalNodeStrategy strategy;
    private String name;

    public BaseLexicalNode(String name, LexicalNodeStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public void addBranch(NodeBranch branch) {
        branches.add(branch);
    }

    @Override
    public Token process(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        Optional<CodeCharacter> nextChar = reader.peek();
        if (nextChar.isEmpty()){
            return strategy.onEndOfFile(reader, currentLexeme);
        }

        CodeCharacter ch = nextChar.get();
        for (NodeBranch branch : branches){
            if (branch.test(ch)){
                updateLexeme(reader, currentLexeme);
                return branch.delegate(reader, currentLexeme);
            }
        }

        return strategy.onNoBranchSelected(reader, currentLexeme, ch);
    }

    protected void updateLexeme(SourceCodeReader reader, Lexeme currentLexeme) {
        currentLexeme.add(reader.next()
                .orElseThrow(() ->
                        new RuntimeException("Reader was able to peek but not to get next. Maybe it was modified in the middle")
                ));
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
