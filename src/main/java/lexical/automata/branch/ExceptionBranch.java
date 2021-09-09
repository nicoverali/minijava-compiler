package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.AutomataLexeme;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;

public class ExceptionBranch implements NodeBranch {

    private LexicalFilter filter;
    private final String errorMsg;

    public ExceptionBranch(LexicalFilter filter, String errorMsg){
        this.filter = filter;
        this.errorMsg = errorMsg;
    }

    @Override
    public void setFilter(LexicalFilter filter) {
        this.filter = filter;
    }

    @Override
    public void setNextNode(LexicalNode nextNode) {}

    @Override
    public boolean test(CodeCharacter character) {
        return filter.test(character.getValue());
    }

    @Override
    public Token delegate(SourceCodeReader reader) throws LexicalException {
        if (shouldDelegate(reader)){
            CodeLine line = reader.getCurrentLine().orElse(null);
            int column = getColumnNumber(reader);
            throw new LexicalException(errorMsg, AutomataLexeme.empty(line), column);
        } else {
            return null;
        }
    }

    private int getColumnNumber(SourceCodeReader reader) {
        return reader.peek().map(CodeCharacter::getColumnNumber)
                .orElseGet(() -> reader.getCurrentLine()
                                .map(CodeLine::getSize)
                .orElse(0));
    }

    private boolean shouldDelegate(SourceCodeReader reader){
        return reader.peek()
                .map(character -> filter.test(character.getValue()))
                .orElse(false);
    }
}
