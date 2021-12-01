package semantic.ast.expression.literal;

import asm.ASMWriter;
import semantic.ast.asm.ASMContext;
import semantic.symbol.attribute.type.PrimitiveType;

import java.nio.charset.StandardCharsets;

public class StringLiteralNode extends LiteralNode{

    public StringLiteralNode(PrimitiveType type) {
        super(type);
    }

    @Override
    public void generate(ASMContext context, ASMWriter writer) {
        char[] string = this.getType().getToken().getLexeme().toCharArray();
        writer.writeln("RMEM 1\t;\tReservamos espacio para el valor de retorno");
        writer.writeln("PUSH %s\t;\tCargamos el tamano de la String mas finalizador", string.length);
        writer.writeln("PUSH %s\t;\tCargamos la direccion de malloc", context.getMallocLabel());
        writer.writeln("CALL");

        for (int i = 0; i < string.length; i++) {
            writer.writeln("DUP\t;\tDuplicamos para no perder puntero");
            writer.writeln("PUSH %s\t;\tCargamos caracter '%c'", (int) string[i], string[i]);
            writer.writeln("STOREREF %s\t;\tGuardamos el caracter", i);
        }

        writer.writeln("DUP\t;\tDuplicamos para no perder puntero");
        writer.writeln("PUSH 0\t;\tCargamos el valor nulo finalizador de String");
        writer.writeln("STOREREF %s\t;\tGuardamos caracter finalizador", string.length);
    }
}
