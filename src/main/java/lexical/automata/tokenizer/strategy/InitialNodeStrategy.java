package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class InitialNodeStrategy implements TokenizerNodeStrategy {
    @Override
    public Optional<Token> onNoBranchSelected(@NotNull Lexeme lexeme, @NotNull CodeCharacter currentCharacter) throws LexicalException {
        String finalLexeme = lexeme.getLexeme()+currentCharacter.getValue();
        throw new LexicalException("Illegal character", finalLexeme, currentCharacter.getCodeLine().toString(), currentCharacter.getLineNumber(), currentCharacter.getColumnNumber());
    }

    @Override
    public Optional<Token> onEndOfFile(@NotNull Lexeme lexeme, @Nullable CodeLine currentLine) throws LexicalException {
        return Optional.empty();
    }
}
