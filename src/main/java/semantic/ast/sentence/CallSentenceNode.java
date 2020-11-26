package semantic.ast.sentence;

import semantic.ast.LocalScope;
import semantic.ast.access.AccessNode;
import semantic.ast.LocalVariable;
import semantic.ast.sentence.visitor.SentenceVisitor;

import java.util.List;

public class CallSentenceNode implements SentenceNode {

    private final AccessNode access;

    public CallSentenceNode(AccessNode access) {
        this.access = access;
    }

    @Override
    public void validate(LocalScope scope) {
        // TODO
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }
}
