package semantic.ast.expression.unary;

import asm.ASMWriter;
import lexical.Token;
import lexical.TokenType;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

import java.util.IllegalFormatCodePointException;

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

    @Override
    public Token toToken() {
        return operator;
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        expression.generate(context, writer);
        if (operator.getType().equals(TokenType.OP_MINUS)){
            writer.writeln("NEG");
        }
    }
}
