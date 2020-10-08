package lexical.automata.tokenizer;

import lexical.automata.LexicalNodeBuilder;
import lexical.automata.filter.LexicalFilter;

public class InitialTokenizerNodeBuilder extends LexicalNodeBuilder<TokenizerNode, InitialTokenizerBranchBuilder> {

    InitialTokenizerNode buildingNode = new InitialTokenizerNode();

    @Override
    protected InitialTokenizerBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new InitialTokenizerBranchBuilder(this, filter);
    }

    @Override
    public InitialTokenizerNode build() {
        InitialTokenizerNode builtNode = buildingNode;
        buildingNode = new InitialTokenizerNode();
        return builtNode;
    }
}
