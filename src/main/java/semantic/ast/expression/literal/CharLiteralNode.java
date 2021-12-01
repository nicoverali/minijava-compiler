package semantic.ast.expression.literal;

import asm.ASMWriter;
import semantic.ast.asm.ASMContext;
import semantic.symbol.attribute.type.PrimitiveType;

public class CharLiteralNode extends LiteralNode {

    public CharLiteralNode(PrimitiveType type) {
        super(type);
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        char character = this.getType().getToken().getLexeme().charAt(0);
        writer.writeln("PUSH %s\t;\tValor de char", (int) character);
    }
}
