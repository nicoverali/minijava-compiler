package semantic.ast.expression.binary;

import lexical.Token;
import lexical.TokenType;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

import java.util.Set;

import static semantic.symbol.attribute.type.PrimitiveType.INT;

public class ArithmeticBinaryExpression extends BinaryExpression{

    public ArithmeticBinaryExpression(ExpressionNode leftExpression, ExpressionNode rightExpression, Token operator) {
        super(leftExpression, rightExpression, operator);
    }

    @Override
    public Type getType() {
        return INT(getOperator());
    }

    @Override
    protected Set<Type> getValidTypes() {
        return Set.of(INT());
    }

    @Override
    protected String getOperatorASM(TokenType operatorType) {
        switch (operatorType) {
            case OP_MULT: return "MUL";
            case OP_PLUS: return "ADD";
            case OP_MINUS: return "SUB";
            case OP_DIV: return "DIV";
            case OP_MOD: return "MOD";
            default: return "NOP\t;\tInvalid operator";
        }
    }
}
