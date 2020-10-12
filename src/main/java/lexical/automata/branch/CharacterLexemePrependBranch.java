package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.AutomataToken;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * This type of {@link NodeBranch} delegates processing of characters as usual, but when the node returns
 * a Token or throws a LexicalException, it prepends the {@link SourceCodeReader} current character to their {@link Lexeme}.
 */
public class CharacterLexemePrependBranch implements NodeBranch<AutomataToken> {

    private final NodeBranch<AutomataToken> decorated;

    public CharacterLexemePrependBranch(NodeBranch<AutomataToken> decorated) {
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
        Optional<CodeCharacter> currentChar = reader.peek();
        return currentChar.map(character -> this.delegate(reader, character))
                .orElseGet(() -> decorated.delegate(reader));
    }

    private AutomataToken delegate(SourceCodeReader reader, CodeCharacter character) throws LexicalException {
        try {
            return prepend(decorated.delegate(reader), character);
        } catch (LexicalException e){
            throw prepend(e, character);
        }
    }

    private AutomataToken prepend(AutomataToken token, CodeCharacter character){
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
