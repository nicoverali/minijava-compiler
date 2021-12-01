package semantic.ast.expression.literal;

import asm.ASMWriter;
import lexical.Token;
import semantic.ast.asm.ASMContext;
import semantic.ast.expression.OperandNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.PrimitiveType;
import semantic.symbol.attribute.type.Type;

public abstract class LiteralNode implements OperandNode {

    private final PrimitiveType type;

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
