package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import lexical.automata.Lexeme;

public class AcceptorStrategy implements LexicalNodeStrategy {

    private TokenType returnType = null;

    public AcceptorStrategy() {}

    public AcceptorStrategy(TokenType returnType) {
        this.returnType = returnType;
    }

    @Override
    public AutomataToken onNoBranchSelected(SourceCodeReader reader, Lexeme currentLexeme, CodeCharacter nextCharacter) throws LexicalException {
        return getTokenFromLexeme(reader, currentLexeme);
    }

    @Override
    public AutomataToken onEndOfFile(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        return getTokenFromLexeme(reader, currentLexeme);
    }

    private AutomataToken getTokenFromLexeme(SourceCodeReader reader, Lexeme lexeme) {
        if (returnType == null) return null;

        CodeLine firstLine = lexeme.getFirst().map(CodeCharacter::getCodeLine)
                .orElse(reader.getLastLine()
                        .orElse(null));
        CodeCharacter firstCharacter = lexeme.getFirst()
                .orElse(reader.getLastCharacter()
                        .orElse(null));

        return new AutomataToken(lexeme, returnType, firstLine, firstCharacter);
    }
}
