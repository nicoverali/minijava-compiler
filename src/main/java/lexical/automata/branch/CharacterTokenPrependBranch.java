package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.AutomataToken;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This type of {@link NodeBranch} delegates processing of characters as usual, but when the node returns
 * a Token it prepends the {@link SourceCodeReader} current character to it.
 */
public class CharacterTokenPrependBranch implements NodeBranch<AutomataToken> {

    private final NodeBranch<AutomataToken> decorated;

    public CharacterTokenPrependBranch(NodeBranch<AutomataToken> decorated) {
        this.decorated = decorated;
    }

    @Override
    public void setFilter(LexicalFilter filter) {
        decorated.setFilter(filter);
    }

    @Override
    public void setNextNode(LexicalNode<AutomataToken> nextNode) {
        decorated.setNextNode(nextNode);
    }

    @Override
    public @Nullable AutomataToken delegate(SourceCodeReader reader) throws LexicalException {
        Optional<CodeCharacter> currentCharacter = reader.peek();
        return currentCharacter.map(character -> prepend(decorated.delegate(reader), character))
                .orElseGet(() -> decorated.delegate(reader));
    }

    private AutomataToken prepend(AutomataToken token, CodeCharacter character){
        if (token != null) token.prepend(character);
        return token;
    }
}
