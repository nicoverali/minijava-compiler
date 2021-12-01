package semantic;

import asm.ASMWriter;
import semantic.ast.asm.ASMContext;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public interface Variable {

    /**
     * @return the {@link Type} of this variable
     */
    Type getType();

    /**
     * @return the {@link NameAttribute} of this variable
     */
    NameAttribute getNameAttribute();

    /**
     * Generates the ASM code to load this variable on top of the stack.
     *
     * @param context ASM context
     * @param writer ASM writer
     */
    void generateASMLoad(ASMContext context, ASMWriter writer);

    /**
     * Generates the ASM code to store to this variable the top of the stack.
     *
     * @param context ASM context
     * @param writer ASM writer
     */
    void generateASMStore(ASMContext context, ASMWriter writer);

}
