package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.Lexeme;
import lexical.automata.branch.filter.LexicalFilter;
import util.Iterables;

public class ThrowNodeBranch extends DefaultNodeBranch{

    private String errorMsg;

    public ThrowNodeBranch(LexicalFilter filter, String errorMsg) {
        super(filter);
        this.errorMsg = errorMsg;
    }

    @Override
    public Token delegate(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        CodeCharacter error = Iterables.getLast(currentLexeme.getAllCharacters());
        currentLexeme.pop();

        String errorLine = error.getCodeLine().toString();
        throw new LexicalException(errorMsg, currentLexeme.toString(), errorLine, error.getLineNumber(), error.getColumnNumber());
    }
}
