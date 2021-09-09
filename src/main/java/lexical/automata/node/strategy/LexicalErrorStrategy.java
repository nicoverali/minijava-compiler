package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.AutomataLexeme;
import lexical.automata.node.LexicalNodeStrategy;

/**
 * With this {@link LexicalNodeStrategy}, if the node cannot delegate processing of characters, it detects
 * a lexical error and throws a {@link LexicalException}
 */
public class LexicalErrorStrategy implements LexicalNodeStrategy {

    private final String errorMsg;

    public LexicalErrorStrategy(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public Token onNoBranchSelected(SourceCodeReader reader, CodeCharacter currentCharacter) throws LexicalException {
        throw new LexicalException(errorMsg, AutomataLexeme.empty(currentCharacter.getCodeLine()), currentCharacter.getColumnNumber());
    }

    @Override
    public Token onEndOfFile(SourceCodeReader reader, CodeLine currentLine) throws LexicalException {
        int columnNumber = currentLine != null ? currentLine.getSize() : 0;
        throw new LexicalException(errorMsg, AutomataLexeme.empty(currentLine), columnNumber);
    }
}
