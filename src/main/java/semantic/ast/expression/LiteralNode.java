package semantic.ast.expression;

import lexical.Token;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

public class LiteralNode implements OperandNode {

    private PrimitiveType type;

    public LiteralNode(PrimitiveType type) {
        this.type = type;
    }


    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Token toToken() {
        return type.getToken();
    }

    @Override
    public void validate(Scope scope) {
        // All literal nodes are valid
    }
}
