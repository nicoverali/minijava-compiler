package semantic.ast.expression;

import lexical.Token;
import semantic.ast.LocalScope;
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
    public void validate(LocalScope scope) {
        // TODO
    }
}
