package semantic.ast.expression;

import lexical.Token;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.NullType;
import semantic.symbol.attribute.type.Type;

public class NullNode implements OperandNode{

    private final NullType nullType;

    public NullNode(NullType nullType) {
        this.nullType = nullType;
    }

    @Override
    public void validate(Scope scope) {
        // Every null node is valid
    }

    @Override
    public Type getType() {
        return nullType;
    }

    @Override
    public Token toToken() {
        return nullType.getToken();
    }

}
