package asm.generator;

import asm.ASMWriter;
import asm.offset.ASMOffsetsGenerator;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.SymbolTable;

import java.util.Collection;

import static asm.ASMLabeler.label;

public class ASMGenerator {

    private static final SymbolTable ST = SymbolTable.getInstance();

    private static final String INIT =
            "PUSH simple_heap_init\n" +
            "CALL\n" +
            "PUSH %s\n" +
            "CALL\n" +
            "HALT\n" +
            "\n" +
            "simple_heap_init:\n" +
            "\tRET 0\t; Retorna inmediatamente\n" +
            "\n" +
            "simple_malloc:\n" +
            "\tLOADFP\t; Inicialización unidad\n" +
            "\tLOADSP\n" +
            "\tSTOREFP ; Finaliza inicialización del RA\n" +
            "\tLOADHL\t; hl\n" +
            "\tDUP\t; hl\n" +
            "\tPUSH 1\t; 1\n" +
            "\tADD\t; hl+1\n" +
            "\tSTORE 4 ; Guarda el resultado (un puntero a la primer celda de la región de memoria)\n" +
            " \tLOAD 3\t; Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)\n" +
            "\tADD\n" +
            "\tSTOREHL ; Mueve el heap limit (hl). Expande el heap\n" +
            "\tSTOREFP\n" +
            "\tRET 1\t; Retorna eliminando el parámetro";

    public void generate(ASMWriter writer){
        generateInit(writer);
        generateClasses(writer);
        writer.close();
    }

    private void generateInit(ASMWriter writer) {
        MethodSymbol main = ST.getMainMethod();
        writer.writeln(String.format(INIT, label(main)));
    }

    private void generateClasses(ASMWriter writer) {
        Collection<ClassSymbol> classes = ST.getAllClasses();
        ASMClassGenerator classGenerator = new ASMClassGenerator();
        ASMOffsetsGenerator offsetGenerator = new ASMOffsetsGenerator(classes);
        offsetGenerator.generateOffsets();

        for (ClassSymbol clazz : classes) {
            classGenerator.generate(clazz, offsetGenerator, writer);
        }
    }
}
