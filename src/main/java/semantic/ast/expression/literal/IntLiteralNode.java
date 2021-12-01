package semantic.ast.expression.literal;

import asm.ASMWriter;
import semantic.ast.asm.ASMContext;
import semantic.symbol.attribute.type.PrimitiveType;

public class IntLiteralNode extends LiteralNode{

    public IntLiteralNode(PrimitiveType type) {
        super(type);
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        String number = this.getType().getToken().getLexeme();
        writer.writeln("PUSH %s\t;\tValor de int", number);
    }
}
