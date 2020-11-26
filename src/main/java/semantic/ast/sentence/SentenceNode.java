package semantic.ast.sentence;

import semantic.ast.ASTNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

import java.util.List;

public interface SentenceNode extends ASTNode {

    /**
     * Accepts a {@link SentenceVisitor}
     *
     * @param visitor a {@link SentenceVisitor}
     */
    void accept(SentenceVisitor visitor);

}
