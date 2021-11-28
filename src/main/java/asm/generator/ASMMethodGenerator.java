package asm.generator;

import asm.ASMWriter;
import asm.offset.ASMOffsetsGenerator;
import semantic.ast.block.BlockNode;
import semantic.symbol.MethodSymbol;

import static asm.ASMLabeler.label;

public class ASMMethodGenerator {

    public void generate(MethodSymbol method, ASMOffsetsGenerator offsets, ASMWriter writer) {
        generateMethodHeader(method, writer);
        writer.indent();
        generateMethodIntro(writer);
        generateSentence(method.getBlock(), offsets, writer);
        generateMethodOutro(method, writer);
        writer.unindent();
        writer.writeln();
    }

    private void generateMethodHeader(MethodSymbol method, ASMWriter writer) {
        writer.writeln(label(method)+":");
    }

    private void generateMethodIntro(ASMWriter writer){
        writer.writeln("LOADFP\t; Inicialización unidad");
        writer.writeln("LOADSP");
        writer.writeln("STOREFP ; Finaliza inicialización del RA");
    }

    private void generateSentence(BlockNode block, ASMOffsetsGenerator offsets, ASMWriter writer) {

    }

    private void generateMethodOutro(MethodSymbol method, ASMWriter writer) {
        int retOffset = method.getParameters().size();
        if (method.isDynamic()) retOffset++;

        writer.writeln("STOREFP\t; Reestablecer FP a RA anterior");
        writer.writeln("RET %s; Subir la stack n posiciones para volver al RA anterior", retOffset);
    }

}
