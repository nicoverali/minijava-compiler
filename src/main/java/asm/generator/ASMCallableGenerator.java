package asm.generator;

import asm.ASMPredefinedMethods;
import asm.ASMWriter;
import asm.offset.ASMOffsetsGenerator;
import semantic.ast.asm.ASMContext;
import semantic.symbol.CallableSymbol;
import semantic.symbol.predefined.PredefinedMethod;

import static asm.ASMLabeler.label;
import static semantic.symbol.attribute.type.VoidType.VOID;

public class ASMCallableGenerator {

    public void generate(CallableSymbol callable, ASMOffsetsGenerator offsets, ASMWriter writer) {
        generateMethodHeader(callable, writer);
        writer.indent();
        generateMethodIntro(writer);
        generateSentence(callable, offsets, writer);
        generateMethodOutro(callable, writer);
        writer.unindent();
        writer.writeln();
    }

    private void generateMethodHeader(CallableSymbol callable, ASMWriter writer) {
        writer.writeln(label(callable)+":");
    }

    private void generateMethodIntro(ASMWriter writer){
        writer.writeln("LOADFP\t;\tInicialización unidad");
        writer.writeln("LOADSP");
        writer.writeln("STOREFP\t;\tFinaliza inicialización del RA");
    }

    private void generateSentence(CallableSymbol callable, ASMOffsetsGenerator offsets, ASMWriter writer) {
        if (callable instanceof PredefinedMethod) {
            ASMPredefinedMethods.getCodeOf((PredefinedMethod) callable).forEach(writer::writeln);
            return;
        }

        ASMContext context = new ASMContext(callable, offsets, "simple_malloc");
        callable.getBlock().getSentences().forEach(s -> s.generate(context, writer));

        if (callable.getReturnType().equals(VOID())) {
            writer.writeln("FMEM %s\t;\tLiberamos el espacio de las variables locales", context.numberOfVariables());
        }
    }

    private void generateMethodOutro(CallableSymbol callable, ASMWriter writer) {
        // Non-void callables will produce the outro in the return statement
        if (!callable.getReturnType().equals(VOID())) return;

        int retOffset = callable.getParameters().size();
        if (isCallableDynamic(callable)) retOffset++;

        writer.writeln("STOREFP\t;\tReestablecer FP a RA anterior");
        writer.writeln("RET %s\t;\tSubir la stack n posiciones para volver al RA anterior", retOffset);
    }

    private boolean isCallableDynamic(CallableSymbol callable) {
        return callable.getStaticAttribute().getValue().equals(false);
    }

}
