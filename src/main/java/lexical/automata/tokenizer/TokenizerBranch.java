package lexical.automata.tokenizer;

import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;

public class TokenizerBranch implements NodeBranch<TokenizerNode> {

    private final LexicalFilter filter;
    private final TokenizerNode nextNode;
    private final boolean shouldStoreCharacter;

    public TokenizerBranch(LexicalFilter filter, TokenizerNode nextNode, boolean shouldStoreCharacter) {
        this.filter = filter;
        this.nextNode = nextNode;
        this.shouldStoreCharacter = shouldStoreCharacter;
    }

    @Override
    public LexicalFilter getFilter() {
        return filter;
    }

    @Override
    public TokenizerNode getNextNode() {
        return nextNode;
    }

    public boolean shouldStoreCharacter() {
        return shouldStoreCharacter;
    }
}
