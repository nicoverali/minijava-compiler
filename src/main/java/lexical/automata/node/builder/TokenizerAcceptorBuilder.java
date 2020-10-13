package lexical.automata.node.builder;

import lexical.Token;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import lexical.automata.LexicalNode;
import lexical.automata.node.DefaultLexicalNode;
import lexical.automata.node.strategy.TokenizeStrategy;

/**
 * This builder will create a {@link LexicalNode} without any branch, that just returns a new {@link Token}
 */
public class TokenizerAcceptorBuilder {

    private final TokenType type;

    public TokenizerAcceptorBuilder(TokenType type){
        this.type = type;
    }

    /**
     * Builds a new {@link LexicalNode} without any {@link lexical.automata.NodeBranch}.
     */
    public LexicalNode<AutomataToken> build(){
        DefaultLexicalNode<AutomataToken> node = new DefaultLexicalNode<>("Accepts a "+ type +" token");
        node.setStrategy(new TokenizeStrategy(type));
        return node;
    }

}
