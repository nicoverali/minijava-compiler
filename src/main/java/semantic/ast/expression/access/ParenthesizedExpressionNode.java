package semantic.ast.expression.access;

import lexical.Token;
import semantic.ast.expression.ExpressionNode;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.Type;

public class ParenthesizedExpressionNode extends BaseAccessNode{

    private final ExpressionNode expression;

    public ParenthesizedExpressionNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public ParenthesizedExpressionNode(ChainNode chain, ExpressionNode expression) {
        super(chain);
        this.expression = expression;
    }

    @Override
    public Type getType() {
        return expression.getType();
    }

    @Override
    public Token toToken() {
        return expression.toToken();
    }

    @Override
    protected void validateAccess(Scope scope) {

    }
}