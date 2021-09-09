package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.NodeBranch;

import java.util.Optional;

/**
 * This type of {@link NodeBranch} delegates processing of characters as usual, but when the node returns
 * a Token or throws a LexicalException, it prepends the {@link SourceCodeReader} current character to their lexeme.
 */
public class CharacterLexemePrependBranch extends NodeBranchDecorator {

    public CharacterLexemePrependBranch(NodeBranch decorated) {
        super(decorated);
    }

    @Override
    public Token delegate(SourceCodeReader reader) throws LexicalException {
        Optional<CodeCharacter> currentChar = reader.peek();
        return currentChar.map(character -> this.delegate(reader, character))
                .orElseGet(() -> decorated.delegate(reader));
    }

    private Token delegate(SourceCodeReader reader, CodeCharacter character) throws LexicalException {
        try {
            return prepend(decorated.delegate(reader), character);
        } catch (LexicalException e){
            throw prepend(e, character);
        }
    }

    private Token prepend(Token token, CodeCharacter character){
        if (token != null) token.prependLexeme(character);
        return token;
    }

    private LexicalException prepend(LexicalException exception, CodeCharacter character){
        exception.prependLexeme(character);
        return exception;
    }

    @Override
    public String toString() {
        return decorated + " (Store in Lexeme)";
    }
}
