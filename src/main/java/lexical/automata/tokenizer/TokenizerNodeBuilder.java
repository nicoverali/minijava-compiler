package lexical.automata.tokenizer;

import lexical.automata.LexicalNodeBuilder;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.tokenizer.strategy.TokenizerNodeStrategy;
import org.jetbrains.annotations.NotNull;

public class TokenizerNodeBuilder extends LexicalNodeBuilder<TokenizerNode, TokenizerBranchBuilder> {

    TokenizerNode buildingNode;
    private final TokenizerNodeStrategy strategy;

    public TokenizerNodeBuilder(@NotNull TokenizerNodeStrategy strategy) {
        this.strategy = strategy;
        buildingNode = new DefaultTokenizerNode(strategy);
    }

    @Override
    protected TokenizerBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new TokenizerBranchBuilder(this, filter);
    }

    @Override
    public TokenizerNode build() {
        TokenizerNode builtNode = buildingNode;
        buildingNode = new DefaultTokenizerNode(strategy);
        return builtNode;
    }
}
