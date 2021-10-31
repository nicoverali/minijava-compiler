package semantic.ast.expression;

import semantic.SemanticException;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;
import semantic.symbol.finder.AncestorFinder;

import static semantic.symbol.finder.AncestorFinder.isAncestor;

public class CastExpressionNode implements ExpressionNode {

    private final ReferenceType castType;
    private final ExpressionNode expression;

    public CastExpressionNode(ReferenceType castType, ExpressionNode expression) {
        this.castType = castType;
        this.expression = expression;
    }

    @Override
    public void validate(Scope scope) {
        expression.validate(scope);
        Type expType = expression.getType();

        if (!(expType instanceof ReferenceType)) throw new SemanticException("Solo se puede castear tipos clase", castType);
        if (!isAncestor(castType, (ReferenceType) expType)) throw new SemanticException("Tipos incompatibles", castType);
    }

    @Override
    public Type getType() {
        return castType;
    }

}
