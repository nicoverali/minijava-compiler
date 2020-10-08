package lexical.automata.tokenizer;

import lexical.automata.NodeBranchBuilder;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.omitter.OmitterBranch;
import lexical.automata.omitter.OmitterNode;
import lexical.automata.omitter.OmitterNodeBuilder;
import lexical.automata.omitter.TryOmitterBranch;

public class InitialTokenizerBranchBuilder implements NodeBranchBuilder<TokenizerNode, InitialTokenizerNodeBuilder> {

    private final InitialTokenizerNodeBuilder builder;
    private final LexicalFilter filter;
    private boolean shouldStoreCharacter;

    public InitialTokenizerBranchBuilder(InitialTokenizerNodeBuilder builder, LexicalFilter filter) {
        this.builder = builder;
        this.filter = filter;
    }

    @Override
    public InitialTokenizerNodeBuilder thenRepeat() {
        builder.buildingNode.addBranch(new TokenizerBranch(filter, builder.buildingNode, shouldStoreCharacter));
        return builder;
    }

    @Override
    public InitialTokenizerNodeBuilder thenMoveTo(TokenizerNode nextNode) {
        builder.buildingNode.addBranch(new TokenizerBranch(filter, nextNode, shouldStoreCharacter));
        return builder;
    }


    public InitialTokenizerNodeBuilder thenMoveTo(OmitterNode nextNode) {
        builder.buildingNode.addBranch(new OmitterBranch(filter, nextNode));
        return builder;
    }

    @Override
    public InitialTokenizerNodeBuilder thenTry(TokenizerNode nextNode) {
        builder.buildingNode.addTryBranch(new TryTokenizerBranch(filter, nextNode, shouldStoreCharacter));
        return builder;
    }


    public InitialTokenizerNodeBuilder thenTry(OmitterNode nextNode) {
        builder.buildingNode.addTryBranch(new TryOmitterBranch(filter, nextNode));
        return builder;
    }

    /**
     * Indicates that this branch should store the current character as part of the lexeme.
     *
     * @return this {@link InitialTokenizerBranchBuilder} to keep building the branch
     */
    public InitialTokenizerBranchBuilder storeCharacter(){
        shouldStoreCharacter = true;
        return this;
    }

    /**
     * Indicates that this branch should skip the current character as part of the lexeme.
     *
     * @return this {@link InitialTokenizerBranchBuilder} to keep building the branch
     */
    public InitialTokenizerBranchBuilder skipCharacter(){
        shouldStoreCharacter = false;
        return this;
    }
}
