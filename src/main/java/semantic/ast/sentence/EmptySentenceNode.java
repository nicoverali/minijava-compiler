package semantic.ast.sentence;

import semantic.ast.LocalScope;
import semantic.ast.LocalVariable;
import semantic.ast.sentence.visitor.SentenceVisitor;

import java.util.List;

public class EmptySentenceNode implements SentenceNode {

    @Override
    public void validate(LocalScope scope) {
        // Empty sentences are always valid
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
