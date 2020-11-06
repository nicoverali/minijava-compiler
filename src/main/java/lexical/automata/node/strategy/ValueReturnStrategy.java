package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.node.LexicalNodeStrategy;

/**
 * This strategy will always return a certain value if the node cannot delegate or the end of file has been reached.
 *
 * @param <T> type of value returned by this strategy
 */
public class ValueReturnStrategy<T> implements LexicalNodeStrategy<T> {

    private final T value;

    public ValueReturnStrategy(T value){
        this.value = value;
    }

    @Override
    public  T onNoBranchSelected(SourceCodeReader reader,  CodeCharacter currentCharacter) throws LexicalException {
        return value;
    }

    @Override
    public  T onEndOfFile(SourceCodeReader reader,  CodeLine currentLine) throws LexicalException {
        return value;
    }

}
