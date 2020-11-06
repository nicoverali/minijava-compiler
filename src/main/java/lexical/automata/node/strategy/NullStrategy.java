package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.node.LexicalNodeStrategy;

/**
 * This {@link LexicalNodeStrategy} does not do anything if the node cannot delegate, and simply returns a
 * <code>null</code> value.
 *
 * @param <T> type of element returned by the {@link lexical.automata.LexicalNode}
 */
public class NullStrategy<T> implements LexicalNodeStrategy<T> {

    @Override
    public  T onNoBranchSelected(SourceCodeReader reader,  CodeCharacter currentCharacter) throws LexicalException {
        return null;
    }

    @Override
    public  T onEndOfFile(SourceCodeReader reader,  CodeLine currentLine) throws LexicalException {
        return null;
    }

}
