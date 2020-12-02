package semantic.ast.expression;

import lexical.Token;
import semantic.ast.Scope;
import semantic.symbol.TopLevelSymbol;
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
    public Type getType(Scope scope) {
        return type;
    }

    @Override
    public void validate(Scope scope) {
        // All literal nodes are valid
    }

    @Override
    public ExpressionNode instantiate(TopLevelSymbol container, String newType) {
        return this; // No literal node is generic
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        return false; // No literal node is generic
    }
}
