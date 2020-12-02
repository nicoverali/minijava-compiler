package semantic.ast.sentence;

import semantic.ast.ASTNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;

public interface SentenceNode extends ASTNode {

    /**
     * Accepts a {@link SentenceVisitor}
     *
     * @param visitor a {@link SentenceVisitor}
     */
    void accept(SentenceVisitor visitor);

    @Override
    SentenceNode instantiate(TopLevelSymbol container, String newType);
}
