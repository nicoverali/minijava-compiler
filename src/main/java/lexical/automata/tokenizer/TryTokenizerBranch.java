package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.TryNodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TryTokenizerBranch implements TryNodeBranch<TokenizerNode, Token> {

    private static final int READ_AHEAD_LIMIT = 50; // This is just an estimation

    private final LexicalFilter filter;
    private final TokenizerNode nextNode;
    private final boolean shouldStoreCharacter;

    public TryTokenizerBranch(LexicalFilter filter, TokenizerNode nextNode, boolean shouldStoreCharacter) {
        this.filter = filter;
        this.nextNode = nextNode;
        this.shouldStoreCharacter = shouldStoreCharacter;
    }



    @Override
    @Nullable
    public Token tryDelegate(SourceCodeReader reader) {
        reader.mark(READ_AHEAD_LIMIT);
        Optional<CodeCharacter> nextChar = reader.next();

        try{
            Token token = nextNode.tokenize(reader);
            nextChar.ifPresent(character -> checkIfShouldPrependLexeme(character, token));
            return token;
        } catch (LexicalException e){
            reader.reset();
            return null;
        }
    }

    private void checkIfShouldPrependLexeme(CodeCharacter character, Token token){
        if (shouldStoreCharacter){
            token.getLexeme().prepend(character.getValue());
        }
    }

    @Override
    public @NotNull LexicalFilter getFilter() {
        return filter;
    }

}
