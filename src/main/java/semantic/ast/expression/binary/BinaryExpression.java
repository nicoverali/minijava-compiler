package semantic.ast.expression.binary;

import asm.ASMWriter;
import lexical.Token;
import lexical.TokenType;
import semantic.SemanticException;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.Type;

import java.util.Set;

public abstract class BinaryExpression implements ExpressionNode {

    private final ExpressionNode leftExpression;
    private final ExpressionNode rightExpression;
    private final Token operator;

    public BinaryExpression(ExpressionNode leftExpression, ExpressionNode rightExpression, Token operator) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.operator = operator;
    }

    @Override
    public void validate(Scope scope) {
        leftExpression.validate(scope);
        rightExpression.validate(scope);

        Set<Type> validTypes = getValidTypes();
        if (!validTypes.contains(leftExpression.getType())){
            throw new SemanticException("Operando izquierdo incorrecto", operator);
        }
        if (!validTypes.contains(rightExpression.getType())){
            throw new SemanticException("Operando derecho incorrecto", operator);
        }
    }

    abstract protected Set<Type> getValidTypes();

    public Token getOperator(){
        return operator;
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

    abstract protected String getOperatorASM(TokenType operatorType);

}
