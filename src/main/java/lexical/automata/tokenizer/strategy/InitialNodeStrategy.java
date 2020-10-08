package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InitialNodeStrategy implements TokenizerNodeStrategy {

    @Override
    public @NotNull Token onNoBranchSelected(@NotNull CodeCharacter currentCharacter) throws LexicalException {
        String line = currentCharacter.getCodeLine().toString();
        int lineNumber = currentCharacter.getLineNumber();
        Lexeme lexeme = Lexeme.create(currentCharacter.getValue()+"", lineNumber);
        throw new LexicalException("Illegal character", lexeme, line, lineNumber, currentCharacter.getColumnNumber());
    }

    @Override
    public @NotNull Token onEndOfFile(@Nullable CodeLine currentLine) throws LexicalException {
        int lineNumber = currentLine != null ? currentLine.getLineNumber() : 0;
        return new Token(TokenType.EOF, Lexeme.empty(lineNumber));
    }
}
