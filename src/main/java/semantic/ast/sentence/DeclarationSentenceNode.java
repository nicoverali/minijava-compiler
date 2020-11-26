package semantic.ast.sentence;

import semantic.ast.LocalScope;
import semantic.ast.LocalVariable;
import semantic.ast.sentence.visitor.SentenceVisitor;

import java.util.ArrayList;
import java.util.List;

public class DeclarationSentenceNode implements SentenceNode {

    private final List<LocalVariable> declarations = new ArrayList<>();

    @Override
    public void validate(LocalScope scope) {
        // TODO
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
