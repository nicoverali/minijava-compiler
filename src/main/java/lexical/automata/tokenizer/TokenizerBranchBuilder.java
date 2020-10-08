package lexical.automata.tokenizer;

import lexical.automata.NodeBranchBuilder;
import lexical.automata.filter.LexicalFilter;

public class TokenizerBranchBuilder implements NodeBranchBuilder<TokenizerNode, TokenizerNodeBuilder> {

    private final TokenizerNodeBuilder builder;
    private final LexicalFilter filter;

    private boolean shouldStoreCharacter;

    public TokenizerBranchBuilder(TokenizerNodeBuilder builder, LexicalFilter filter) {
        this.builder = builder;
        this.filter = filter;
    }

    @Override
    public TokenizerNodeBuilder thenRepeat() {
        builder.buildingNode.addBranch(new TokenizerBranch(filter, builder.buildingNode, shouldStoreCharacter));
        return builder;
    }

    @Override
    public TokenizerNodeBuilder thenMoveTo(TokenizerNode nextNode) {
        builder.buildingNode.addBranch(new TokenizerBranch(filter, nextNode, shouldStoreCharacter));
        return builder;
    }

    @Override
    public TokenizerNodeBuilder thenTry(TokenizerNode nextNode) {
        builder.buildingNode.addTryBranch(new TryTokenizerBranch(filter, nextNode, shouldStoreCharacter));
        return builder;
    }

    /**
     * Indicates that this branch should store the current character as part of the lexeme.
     *
     * @return this {@link TokenizerBranchBuilder} to keep building the branch
     */
    public TokenizerBranchBuilder storeCharacter(){
        shouldStoreCharacter = true;
        return this;
    }

    /**
     * Indicates that this branch should skip the current character as part of the lexeme.
     *
     * @return this {@link TokenizerBranchBuilder} to keep building the branch
     */
    public TokenizerBranchBuilder skipCharacter(){
        shouldStoreCharacter = false;
        return this;
    }
}
