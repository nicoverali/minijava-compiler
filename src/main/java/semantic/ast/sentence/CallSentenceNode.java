package semantic.ast.sentence;

import semantic.ast.Scope;
import semantic.ast.access.AccessNode;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;

public class CallSentenceNode implements SentenceNode {

    private final AccessNode access;
    private boolean  hasGenerics;

    public CallSentenceNode(AccessNode access) {
        this.access = access;
    }

    @Override
    public void validate(Scope scope) {
        access.validate(scope);
        hasGenerics = access.hasGenerics(scope);
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SentenceNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics){
            return new CallSentenceNode(access.instantiate(container, newType));
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return hasGenerics;
    }
}
