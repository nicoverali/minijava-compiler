package semantic.ast.expression;

import asm.ASMWriter;
import lexical.Token;
import semantic.ast.asm.ASMContext;
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

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        writer.writeln("PUSH 0\t;\tUn valor null se considera 0");
    }
}
