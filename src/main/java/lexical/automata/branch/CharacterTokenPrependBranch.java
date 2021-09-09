package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.AutomataToken;
import lexical.automata.NodeBranch;

import java.util.Optional;

/**
 * This type of {@link NodeBranch} delegates processing of characters as usual, but when the node returns
 * a Token it prepends the {@link SourceCodeReader} current character to it.
 */
public class CharacterTokenPrependBranch extends NodeBranchDecorator {

    public CharacterTokenPrependBranch(){super();}

    @Override
    public Token delegate(SourceCodeReader reader) throws LexicalException {
        Optional<CodeCharacter> currentCharacter = reader.peek();
        return currentCharacter.map(character -> prepend(decorated.delegate(reader), character))
                .orElseGet(() -> decorated.delegate(reader));
    }

    private Token prepend(Token token, CodeCharacter character){
        if (token != null) token.prepend(character);
        return token;
    }

    @Override
    public String toString() {
        return decorated.toString();
    }
}
