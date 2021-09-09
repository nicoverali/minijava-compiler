package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;

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
    public Token delegate(SourceCodeReader reader) throws LexicalException {
        if (nextNode != null && shouldDelegate(reader)){
            reader.next(); // Consume character
            return nextNode.process(reader);
        } else {
            return null;
        }
    }

    private boolean shouldDelegate(SourceCodeReader reader){
        return reader.peek()
                .map(character -> filter.test(character.getValue()))
                .orElse(false);
    }

    @Override
    public String toString(){
        return "if( "+ filter +" ) -> "+ nextNode;
    }
}
