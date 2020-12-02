package semantic.ast.sentence;

import semantic.ast.Scope;
import semantic.ast.sentence.visitor.SentenceVisitor;
import semantic.symbol.TopLevelSymbol;

public class EmptySentenceNode implements SentenceNode {

    @Override
    public void validate(Scope scope) {
        // Empty sentences are always valid
    }

    @Override
    public EmptySentenceNode instantiate(TopLevelSymbol container, String newType) {
        return this; // Empty sentences never have generics
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return false; // Empty sentences never have generics
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
