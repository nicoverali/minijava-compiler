package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.AutomataToken;
import lexical.automata.Lexeme;

import static lexical.TokenType.EOF;

public class InitialNodeStrategy implements LexicalNodeStrategy {

    private final String errorMsg;

    public InitialNodeStrategy(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public AutomataToken onNoBranchSelected(SourceCodeReader reader, Lexeme currentLexeme, CodeCharacter nextCharacter) throws LexicalException {
        reader.next(); // Consume character to move forward for next request
        currentLexeme.add(nextCharacter);

        String line = nextCharacter.getCodeLine().toString();
        int lineNumber = nextCharacter.getLineNumber();
        int columnNumber = nextCharacter.getColumnNumber();
        throw new LexicalException(errorMsg, currentLexeme.toString(), line, lineNumber, columnNumber);
    }

    @Override
    public AutomataToken onEndOfFile(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        CodeLine firstLine = reader.getLastLine().orElse(null);
        CodeCharacter firstCharacter = reader.getLastCharacter().orElse(null);

        return new AutomataToken(Lexeme.empty(), EOF, firstLine, firstCharacter);
    }
}
