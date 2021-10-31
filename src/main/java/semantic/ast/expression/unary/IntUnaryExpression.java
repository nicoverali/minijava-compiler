package semantic.ast.expression.unary;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

import static semantic.symbol.attribute.type.PrimitiveType.INT;

public class IntUnaryExpression implements ExpressionNode {

    private final ExpressionNode expression;
    private final Token operator;

    public IntUnaryExpression(ExpressionNode expression, Token operator) {
        this.expression = expression;
        this.operator = operator;
    }

    @Override
    public void validate(Scope scope) {
        expression.validate(scope);
        if (!expression.getType().equals(INT())){
            throw new SemanticException("Operando incorrecto", operator);
        }
    }

    @Override
    public Type getType() {
        return INT(operator);
    }
}
