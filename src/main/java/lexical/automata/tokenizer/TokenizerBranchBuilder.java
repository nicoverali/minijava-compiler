package lexical.automata.tokenizer;

import lexical.automata.NodeBranchBuilder;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.omitter.OmitterBranch;
import lexical.automata.omitter.OmitterNode;

public class TokenizerBranchBuilder implements NodeBranchBuilder<TokenizerNode, TokenizerNodeBuilder> {

    private TokenizerNodeBuilder builder;
    private LexicalFilter filter;

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

    /**
     * Adds an {@link OmitterBranch} to the {@link TokenizerNode}. If this branch is selected, then the node
     * will delegate the next characters to it, and then will try to generate a {@link lexical.Token}.
     *
     * @param nextNode next {@link OmitterNode} of the branch
     * @return this {@link TokenizerBranchBuilder} to keep building the branch
     */
    public TokenizerNodeBuilder thenMoveTo(OmitterNode nextNode){
        builder.buildingNode.addBranch(new OmitterBranch(filter, nextNode));
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
