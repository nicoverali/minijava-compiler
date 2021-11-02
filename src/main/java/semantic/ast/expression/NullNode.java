package semantic.ast.expression;

import lexical.Token;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.Type;

public class NullNode implements OperandNode{

    private final Token nullToken;

    public NullNode(Token nullToken) {
        this.nullToken = nullToken;
    }

    @Override
    public void validate(Scope scope) {
        // Every null node is valid
    }

    @Override
    public Type getType() {
        return null;// TODO;
    }

    @Override
    public Token toToken() {
        return nullToken;
    }

}
