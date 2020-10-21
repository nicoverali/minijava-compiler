package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.AutomataLexeme;
import lexical.automata.node.LexicalNodeStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * With this {@link LexicalNodeStrategy}, if the node cannot delegate processing of characters, it detects
 * a lexical error and throws a {@link LexicalException}
 *
 * @param <T> type of element returned by the {@link lexical.automata.LexicalNode}
 */
public class LexicalErrorStrategy<T> implements LexicalNodeStrategy<T> {

    private final String errorMsg;

    public LexicalErrorStrategy(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public T onNoBranchSelected(SourceCodeReader reader, @NotNull CodeCharacter currentCharacter) throws LexicalException {
        throw new LexicalException(errorMsg, AutomataLexeme.empty(currentCharacter.getCodeLine()), currentCharacter.getColumnNumber());
    }

    @Override
    public T onEndOfFile(SourceCodeReader reader, @Nullable CodeLine currentLine) throws LexicalException {
        int columnNumber = currentLine != null ? currentLine.getSize() : 0;
        throw new LexicalException(errorMsg, AutomataLexeme.empty(currentLine), columnNumber);
    }
}
