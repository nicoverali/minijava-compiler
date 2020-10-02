package lexical.automata.tokenizer;

import lexical.automata.LexicalNodeBuilder;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.tokenizer.strategy.TokenizerNodeStrategy;
import org.jetbrains.annotations.NotNull;

public class TokenizerNodeBuilder extends LexicalNodeBuilder<TokenizerNode, TokenizerBranchBuilder> {

    TokenizerNode buildingNode;
    private TokenizerNodeStrategy strategy;

    public TokenizerNodeBuilder(@NotNull TokenizerNodeStrategy strategy) {
        this.strategy = strategy;
        buildingNode = new TokenizerNode(strategy, new NodeBranchContainer<>(), new NodeBranchContainer<>());
    }

    @Override
    protected TokenizerBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new TokenizerBranchBuilder(this, filter);
    }

    @Override
    public TokenizerNode build() {
        TokenizerNode builtNode = buildingNode;
        buildingNode = new TokenizerNode(strategy, new NodeBranchContainer<>(), new NodeBranchContainer<>());
        return builtNode;
    }
}
