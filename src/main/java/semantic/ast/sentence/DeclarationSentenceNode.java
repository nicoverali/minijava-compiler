package semantic.ast.sentence;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.block.LocalVariable;
import semantic.ast.scope.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.sentence.visitor.SentenceVisitor;

public class DeclarationSentenceNode implements SentenceNode {

    private final LocalVariable variable;
    private final Token assignment;
    private final ExpressionNode expression;

    public DeclarationSentenceNode(LocalVariable variable) {
        this(variable, null, null);
    }

    public DeclarationSentenceNode(LocalVariable variable, Token assignment, ExpressionNode expression) {
        this.variable = variable;
        this.assignment = assignment;
        this.expression = expression;
    }

    @Override
    public void validate(Scope scope) {
        variable.validate(scope);
        scope.addLocalVariable(variable);

        if (assignment == null) return;
        expression.validate(scope);

        if (!expression.getType().conforms(variable.getType())){
            throw new SemanticException("La asignacion inicial no conforma con el tipo de variable", assignment);
        }
    }

    @Override
    public void accept(SentenceVisitor visitor) {
        visitor.visit(this);
    }

}
