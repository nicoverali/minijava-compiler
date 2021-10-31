package semantic.ast.expression;

import lexical.Token;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

public class LiteralNode implements OperandNode {

    private Token literalToken;
    private PrimitiveType type;

    public LiteralNode(Token literalToken, PrimitiveType type) {
        this.literalToken = literalToken;
        this.type = type;
    }


    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void validate(Scope scope) {
        // All literal nodes are valid
    }
}
