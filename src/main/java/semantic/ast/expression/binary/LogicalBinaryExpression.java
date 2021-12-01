package semantic.ast.expression.binary;

import lexical.Token;
import lexical.TokenType;
import semantic.SemanticException;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.Type;

import java.util.Set;

import static semantic.symbol.attribute.type.PrimitiveType.BOOLEAN;
import static semantic.symbol.attribute.type.PrimitiveType.INT;

public class LogicalBinaryExpression extends BinaryExpression{


    public LogicalBinaryExpression(ExpressionNode leftExpression, ExpressionNode rightExpression, Token operator) {
        super(leftExpression, rightExpression, operator);
    }

    @Override
    public Type getType() {
        return BOOLEAN(getOperator());
    }

    @Override
    protected Set<Type> getValidTypes() {
        return Set.of(BOOLEAN());
    }

    @Override
    protected String getOperatorASM(TokenType operatorType) {
        switch (operatorType) {
            case OP_AND: return "AND";
            case OP_OR: return "OR";
            default: return "NOP\t;\tInvalid operator";
        }
    }
}
