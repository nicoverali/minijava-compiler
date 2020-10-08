package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NonAcceptorStrategy implements TokenizerNodeStrategy{

    private final String errorMsg;

    /**
     * Creates a new non acceptor strategy which, in case it can not process a character, will
     * throw a {@link LexicalException} with the given <code>errorMsg</code>
     *
     * @param errorMsg an error message which will be added to all thrown {@link LexicalException}
     */
    public NonAcceptorStrategy(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    @Override
    public @NotNull Token onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
        CodeLine line = currentCharacter.getCodeLine();
        Lexeme lexeme = Lexeme.create(currentCharacter.getValue()+"", line.getLineNumber());
        throw new LexicalException(errorMsg, lexeme, line.toString(), line.getLineNumber(), currentCharacter.getColumnNumber());
    }

    @Override
    public @NotNull Token onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        String line = currentLine != null ? currentLine.toString() : "";
        int lineNumber = currentLine != null ? currentLine.getLineNumber() : 0;
        int columnNumber = currentLine != null ? currentLine.getSize() : 0;
        Lexeme lexeme = Lexeme.empty(lineNumber);
        throw new LexicalException(errorMsg, lexeme, line, lineNumber, columnNumber);
    }
}
