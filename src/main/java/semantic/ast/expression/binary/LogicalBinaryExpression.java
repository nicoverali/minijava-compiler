package semantic.ast.expression.binary;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.Type;

import static semantic.symbol.attribute.type.PrimitiveType.BOOLEAN;
import static semantic.symbol.attribute.type.PrimitiveType.INT;

public class LogicalBinaryExpression implements ExpressionNode{

    private final ExpressionNode leftExpression;
    private final ExpressionNode rightExpression;
    private final Token operator;

    public LogicalBinaryExpression(ExpressionNode leftExpression, ExpressionNode rightExpression, Token operator) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operator = operator;
    }


    @Override
    public void validate(Scope scope) {
        leftExpression.validate(scope);
        rightExpression.validate(scope);

        if (!leftExpression.getType().equals(BOOLEAN())){
            throw new SemanticException("Operando izquierdo incorrecto", operator);
        }
        if (!rightExpression.getType().equals(BOOLEAN())){
            throw new SemanticException("Operando derecho incorrecto", operator);
        }
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
