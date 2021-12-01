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

public class ComparisonBinaryExpression extends BinaryExpression{

    public ComparisonBinaryExpression(ExpressionNode leftExpression, ExpressionNode rightExpression, Token operator) {
        super(leftExpression, rightExpression, operator);
    }

    @Override
    public Type getType() {
        return BOOLEAN(getOperator());
    }

    @Override
    protected Set<Type> getValidTypes() {
        return Set.of(INT());
    }

    @Override
    protected String getOperatorASM(TokenType operatorType) {
        switch (operatorType) {
            case OP_GT: return "GT";
            case OP_GTE: return "GE";
            case OP_LT: return "LT";
            case OP_LTE: return "LE";
            case OP_MOD: return "MOD";
            default: return "NOP\t;\tInvalid operator";
        }
    }
}
