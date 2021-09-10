package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import lexical.automata.Lexeme;

import java.util.List;

public class SubLexemeStrategy implements LexicalNodeStrategy{

    private final TokenType returnType;
    private final int startOffset;
    private final int endOffset;

    public SubLexemeStrategy(TokenType returnType, int startOffset) {
        this.returnType = returnType;
        this.startOffset = startOffset;
        this.endOffset = startOffset;
    }

    public SubLexemeStrategy(TokenType returnType, int startOffset, int endOffset) {
        this.returnType = returnType;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @Override
    public AutomataToken onNoBranchSelected(SourceCodeReader reader, Lexeme currentLexeme, CodeCharacter nextCharacter) throws LexicalException {
        return getTokenFromLexeme(currentLexeme);
    }

    @Override
    public AutomataToken onEndOfFile(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        return getTokenFromLexeme(currentLexeme);
    }

    private AutomataToken getTokenFromLexeme(Lexeme lexeme) {
        CodeCharacter firstChar = lexeme.getFirst().orElseThrow(() -> new IllegalStateException("Lexeme does not contain any string"));
        CodeLine firstLine = firstChar.getCodeLine();

        List<CodeCharacter> lexemeChars = lexeme.getAllCharacters();
        Lexeme subLexeme = Lexeme.from(lexemeChars.subList(startOffset, lexemeChars.size()-endOffset));
        return new AutomataToken(subLexeme, returnType, firstLine, firstChar);
    }


}
