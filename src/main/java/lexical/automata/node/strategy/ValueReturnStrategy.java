package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.node.LexicalNodeStrategy;

/**
 * This strategy will always return a certain value if the node cannot delegate or the end of file has been reached.
 */
public class ValueReturnStrategy implements LexicalNodeStrategy {

    private final Token value;

    public ValueReturnStrategy(Token value){
        this.value = value;
    }

    @Override
    public Token onNoBranchSelected(SourceCodeReader reader, CodeCharacter currentCharacter) throws LexicalException {
        return value;
    }

    @Override
    public Token onEndOfFile(SourceCodeReader reader,  CodeLine currentLine) throws LexicalException {
        return value;
    }

}
