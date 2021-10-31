package semantic.ast.sentence;

import semantic.ast.scope.Scope;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class EmptySentenceNode implements SentenceNode {

    @Override
    public void validate(Scope scope) {
        // Empty sentences are always valid
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
