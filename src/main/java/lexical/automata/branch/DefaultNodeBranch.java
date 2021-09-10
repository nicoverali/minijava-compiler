package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.Lexeme;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.branch.filter.LexicalFilter;

public class DefaultNodeBranch implements NodeBranch {

    private LexicalFilter filter;
    private LexicalNode nextNode;

    public DefaultNodeBranch(LexicalFilter filter){
        this.filter = filter;
    }

    @Override
    public void setFilter(LexicalFilter filter) {
        this.filter = filter;
    }

    @Override
    public void setNextNode(LexicalNode nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public boolean test(CodeCharacter character) {
        return filter.test(character.getValue());
    }

    @Override
    public Token delegate(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        if (nextNode == null) throw new IllegalStateException("Set the next node before delegating");
        return nextNode.process(reader, currentLexeme);
    }

    @Override
    public String toString(){
        return "if( "+ filter +" ) -> "+ nextNode;
    }
}
