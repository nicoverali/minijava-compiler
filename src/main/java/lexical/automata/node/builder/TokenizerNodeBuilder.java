package lexical.automata.node.builder;

import lexical.Token;
import lexical.TokenType;
import lexical.automata.NodeBranch;
import lexical.automata.LexicalNode;
import lexical.automata.branch.CharacterLexemePrependBranch;
import lexical.automata.branch.CharacterTokenPrependBranch;
import lexical.automata.filter.LexicalFilter;
import lexical.automata.node.DefaultLexicalNode;
import lexical.automata.node.LexicalNodeStrategy;
import lexical.automata.node.strategy.LexicalErrorStrategy;
import lexical.automata.node.strategy.TokenizeStrategy;

public class TokenizerNodeBuilder extends BaseLexicalNodeBuilder<TokenizerNodeBuilder.TokenizerBranchBuilder>{

    private final DefaultLexicalNode buildingNode;

    public TokenizerNodeBuilder(String nodeName){
        buildingNode = new DefaultLexicalNode(nodeName);
    }

    @Override
    protected TokenizerBranchBuilder createBranchBuilder(LexicalFilter filter) {
        return new TokenizerBranchBuilder(filter, this);
    }

    /**
     * Provides the {@link LexicalNode} with a {@link LexicalNodeStrategy} to know what to do whenever it
     * cannot delegate the processing of characters
     *
     * @param strategy a {@link LexicalNodeStrategy} which will be assign to the node
     * @return the final built {@link LexicalNode}
     */
    public LexicalNode orElse(LexicalNodeStrategy strategy){
        buildingNode.setStrategy(strategy);
        return buildingNode;
    }

    /**
     * Makes the {@link LexicalNode} being built to reject the current state and return a <code>null</code> value,
     * meaning that it could not generate a result.
     *
     * @return the final {@link LexicalNode}
     */
    public LexicalNode orElseReject(){
        return buildingNode;
    }

    /**
     * Makes the {@link LexicalNode} being built return a {@link Token} of the specified type whenever it cannot
     * delegate the processing of characters.
     *
     * @param type {@link TokenType} of tokens returned by the {@link LexicalNode} being built
     * @return the final built {@link LexicalNode}
     */
    public LexicalNode orElseReturnToken(TokenType type){
        buildingNode.setStrategy(new TokenizeStrategy(type));
        return buildingNode;
    }

    /**
     * Makes the {@link LexicalNode} being built to throw a {@link lexical.LexicalException} whenever it cannot
     * delegate the processing of characters
     *
     * @param msg the message of all {@link lexical.LexicalException} thrown by the {@link LexicalNode} being built
     * @return the final built {@link LexicalNode}
     */
    public LexicalNode orElseThrow(String msg){
        buildingNode.setStrategy(new LexicalErrorStrategy(msg));
        return buildingNode;
    }

    public class TokenizerBranchBuilder extends BaseLexicalNodeBuilder.BaseNodeBranchBuilder<TokenizerNodeBuilder, TokenizerBranchBuilder> {


        public TokenizerBranchBuilder(LexicalFilter filter, TokenizerNodeBuilder nodeBuilder) {
            super(filter, nodeBuilder);
            super.decorate(new CharacterTokenPrependBranch());
        }

        @Override
        protected TokenizerBranchBuilder getThis() {
            return this;
        }

        @Override
        protected LexicalNode getBuildingNode() {
            return buildingNode;
        }

        /**
         * Makes the {@link NodeBranch} being built prepend the characters it receives to
         * the lexeme generated by the next node of the branch.
         *
         * @return this branch builder to end the branch creation
         */
        public BaseNodeBranchBuilder<TokenizerNodeBuilder, ?> storeInLexeme(){
            buildingBranch = new CharacterLexemePrependBranch(buildingBranch);
            return this;
        }


    }

}
