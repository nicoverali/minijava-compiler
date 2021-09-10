package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.AutomataToken;
import lexical.automata.Lexeme;

import java.util.Optional;

public class NonAcceptorStrategy implements LexicalNodeStrategy {

    private final String errorMsg;

    public NonAcceptorStrategy(String errorMessage) {
        this.errorMsg = errorMessage;
    }

    @Override
    public AutomataToken onNoBranchSelected(SourceCodeReader reader, Lexeme currentLexeme, CodeCharacter nextCharacter) throws LexicalException {
        String line = nextCharacter.getCodeLine().toString();
        int lineNumber = nextCharacter.getLineNumber();
        int columnNumber = nextCharacter.getColumnNumber();
        throw new LexicalException(errorMsg, currentLexeme.toString(), line, lineNumber, columnNumber);
    }

    @Override
    public AutomataToken onEndOfFile(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        Optional<CodeLine> lastLine = reader.getLastLine();
        String line = lastLine.map(CodeLine::toString).orElse("");
        int lineNumber = lastLine.map(CodeLine::getLineNumber).orElse(0);
        int columnNumber = lastLine.map(CodeLine::getSize).orElse(0);

        throw new LexicalException(errorMsg, currentLexeme.toString(), line, lineNumber, columnNumber);
    }
}
