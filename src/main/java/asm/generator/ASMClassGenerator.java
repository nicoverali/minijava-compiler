package asm.generator;

import asm.ASMLabeler;
import asm.ASMWriter;
import asm.offset.ASMOffsetsGenerator;
import asm.offset.MethodOffsetComparator;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;

import java.util.Collection;
import java.util.stream.Collectors;

public class ASMClassGenerator {

    public void generate(ClassSymbol clazz, ASMOffsetsGenerator offsetGenerator, ASMWriter writer){
        writer.writeln();
        generateClassData(clazz, offsetGenerator, writer);
        writer.writeln();
        generateClassCode(clazz, offsetGenerator, writer);
        writer.writeln();

    }

    private void generateClassData(ClassSymbol clazz, ASMOffsetsGenerator offsetGenerator, ASMWriter writer) {
        String methods = clazz.getAllMethods().values().stream()
                .filter(MethodSymbol::isDynamic) // Only dynamic methods go in the VT
                .sorted(new MethodOffsetComparator(offsetGenerator)) // Sort them from lower offsets to higher offsets
                .map(ASMLabeler::label)
                .collect(Collectors.joining(","));

        writer.writeln(".DATA");
        if (methods.isEmpty()) {
            writer.writeln("VT_%s: NOP", clazz.getName());
        } else {
            writer.writeln("VT_%s: DW %s", clazz.getName(), methods);
        }
    }

    private void generateClassCode(ClassSymbol clazz, ASMOffsetsGenerator offsetGenerator, ASMWriter writer) {
        Collection<MethodSymbol> methods = clazz.getMethods();
        ASMMethodGenerator methodGenerator = new ASMMethodGenerator();

        writer.writeln(".CODE");
        for (MethodSymbol method : methods) {
            methodGenerator.generate(method, offsetGenerator, writer);
        }
    }

}
