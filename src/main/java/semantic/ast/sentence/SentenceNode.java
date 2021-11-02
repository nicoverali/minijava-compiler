package semantic.ast.sentence;

import lexical.Token;
import semantic.ast.ASTNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

public interface SentenceNode extends ASTNode {

    /**
     * Accepts a {@link SentenceVisitor}
     *
     * @param visitor a {@link SentenceVisitor}
     */
    void accept(SentenceVisitor visitor);

    /**
     * @return a {@link Token} representation of this sentence
     */
    Token toToken();
}
