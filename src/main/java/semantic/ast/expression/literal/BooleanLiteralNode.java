package semantic.ast.expression.literal;

import asm.ASMWriter;
import lexical.Token;
import lexical.TokenType;
import semantic.ast.asm.ASMContext;
import semantic.symbol.attribute.type.PrimitiveType;

import static lexical.TokenType.K_TRUE;

public class BooleanLiteralNode extends LiteralNode{

    public BooleanLiteralNode(PrimitiveType type) {
        super(type);
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        Token token = this.getType().getToken();
        String value = token.getType().equals(K_TRUE)
                ? "1"
                : "0";

        writer.writeln("PUSH %s\t;\tValor de boolean", value);
    }
}
