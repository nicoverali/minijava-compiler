package semantic.ast.expression.binary;

import asm.ASMWriter;
import lexical.Token;
import lexical.TokenType;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.Type;

import static semantic.symbol.attribute.type.PrimitiveType.BOOLEAN;

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

        if (!(leftType.conforms(rightType) || rightType.conforms(leftType))){
            throw new SemanticException("Operandos incompatibles", operator);
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

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        leftExpression.generate(context, writer);
        rightExpression.generate(context, writer);
        writer.writeln(getOperatorASM(operator.getType()));
    }

    private String getOperatorASM(TokenType operatorType) {
        switch (operatorType) {
            case OP_EQ: return "EQ";
            case OP_NOTEQ: return "NE";
            default: return "NOP\t;\tInvalid operator";
        }
    }
}
