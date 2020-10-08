package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.DelegateNodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TokenizerBranch implements DelegateNodeBranch<TokenizerNode, Token> {

    private final LexicalFilter filter;
    private final TokenizerNode nextNode;
    private final boolean shouldStoreCharacter;

    public TokenizerBranch(LexicalFilter filter, TokenizerNode nextNode, boolean shouldStoreCharacter) {
        this.filter = filter;
        this.nextNode = nextNode;
        this.shouldStoreCharacter = shouldStoreCharacter;
    }

    @Override
    public @NotNull Token delegate(SourceCodeReader reader) throws LexicalException {
        Optional<CodeCharacter> nextChar = reader.next(); // Consume character
        if (!shouldStoreCharacter) {
            return nextNode.tokenize(reader);
        }
        try{
            Token token = nextNode.tokenize(reader);
            nextChar.ifPresent(character -> token.getLexeme().prepend(character.getValue()));
            return token;
        } catch (LexicalException e){
            nextChar.ifPresent(character -> e.getLexeme().prepend(character.getValue()));
            throw e;
        }
    }

    @Override
    public @NotNull LexicalFilter getFilter() {
        return filter;
    }

}
