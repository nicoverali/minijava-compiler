package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.node.LexicalNodeStrategy;

/**
 * This {@link LexicalNodeStrategy} does not do anything if the node cannot delegate, and simply returns a
 * <code>null</code> value.
 */
public class NullStrategy implements LexicalNodeStrategy {

    @Override
    public Token onNoBranchSelected(SourceCodeReader reader, CodeCharacter currentCharacter) throws LexicalException {
        return null;
    }

    @Override
    public Token onEndOfFile(SourceCodeReader reader,  CodeLine currentLine) throws LexicalException {
        return null;
    }

}
