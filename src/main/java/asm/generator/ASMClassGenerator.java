package asm.generator;

import asm.ASMLabeler;
import asm.ASMWriter;
import asm.offset.ASMOffsetsGenerator;
import asm.offset.MethodOffsetComparator;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.ClassSymbol;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.MethodSymbol;

import java.util.List;
import java.util.stream.Collectors;

import static asm.ASMLabeler.labelVT;

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
            writer.writelnLabeled(labelVT(clazz), "NOP");
        } else {
            writer.writelnLabeled(labelVT(clazz), "DW %s", methods);
        }

        List<String> staticAttr = clazz.getAttributes().stream()
                .filter(AttributeSymbol::isStatic)
                .map(ASMLabeler::label)
                .collect(Collectors.toList());

        for (String label : staticAttr) {
            writer.writelnLabeled(label, "DW 0");
        }
    }

    private void generateClassCode(ClassSymbol clazz, ASMOffsetsGenerator offsetGenerator, ASMWriter writer) {
        ASMCallableGenerator callableGen = new ASMCallableGenerator();

        writer.writeln(".CODE");
        for (MethodSymbol method : clazz.getMethods()) {
            callableGen.generate(method, offsetGenerator, writer);
        }
        for (ConstructorSymbol constructor : clazz.getConstructors()) {
            callableGen.generate(constructor, offsetGenerator, writer);
        }
    }

}
