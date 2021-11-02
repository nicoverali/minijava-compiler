package semantic.ast.expression.binary;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import static semantic.symbol.attribute.type.PrimitiveType.BOOLEAN;
import static semantic.symbol.finder.AncestorFinder.areTypesCompatible;

public class EqualityBinaryExpression implements ExpressionNode{

    private final ExpressionNode leftExpression;
    private final ExpressionNode rightExpression;
    private final Token operator;

    public EqualityBinaryExpression(ExpressionNode leftExpression, ExpressionNode rightExpression, Token operator) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operator = operator;
    }


    @Override
    public void validate(Scope scope) {
        leftExpression.validate(scope);
        rightExpression.validate(scope);

        Type leftType = leftExpression.getType();
        Type rightType = rightExpression.getType();

        if (leftType.equals(rightType)) return;

        if (!(leftType instanceof ReferenceType)) throw new SemanticException("Operando izquierdo incorrecto", operator);
        if (!(rightType instanceof ReferenceType)) throw new SemanticException("Operando derecho incorrecto", operator);
        if (!areTypesCompatible((ReferenceType) leftType, (ReferenceType) rightType)) throw new SemanticException("Operandos incompatibles", operator);
    }

    @Override
    public Type getType() {
        return BOOLEAN(operator);
    }

    @Override
    public Token toToken() {
        return operator;
    }
}
