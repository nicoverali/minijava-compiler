package semantic.ast;

import asm.ASMWriter;

public interface ASMNode {

    /**
     * Generates ASM code that represents this particular node.
     *
     * @param writer a {@link ASMWriter} which will output the code to a file
     */
    void generate(ASMWriter writer);

}