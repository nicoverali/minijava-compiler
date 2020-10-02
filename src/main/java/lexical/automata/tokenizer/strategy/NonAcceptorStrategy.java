package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NonAcceptorStrategy implements TokenizerNodeStrategy{

    private String errorMsg;

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
    public Optional<Token> onNoBranchSelected(@NotNull Lexeme lexeme, @NotNull CodeCharacter currentCharacter) throws LexicalException {
        String finalLexeme = lexeme.getLexeme()+currentCharacter.getValue();
        throw new LexicalException(errorMsg, finalLexeme, currentCharacter.getCodeLine().toString(), currentCharacter.getLineNumber(), currentCharacter.getColumnNumber());
    }

    @Override
    public Optional<Token> onEndOfFile(@NotNull Lexeme lexeme, @Nullable CodeLine currentLine) throws LexicalException {
        if (currentLine != null){
            throw new LexicalException(errorMsg, lexeme.getLexeme(), currentLine.toString(), currentLine.getLineNumber(), currentLine.getSize());
        } else {
            throw new LexicalException(errorMsg, lexeme.getLexeme(), "", 0, 0);
        }
    }
}
