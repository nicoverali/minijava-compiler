package asm.offset;

import semantic.symbol.AttributeSymbol;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;

import java.util.*;
import java.util.stream.Collectors;

import static asm.ASMLabeler.label;

public class ASMOffsetsGenerator {

    private final Collection<ClassSymbol> classes;
    private final Map<String, Integer> methodsOffsets = new HashMap<>();
    private final Map<String, Integer> attrOffsets = new HashMap<>();

    public ASMOffsetsGenerator(Collection<ClassSymbol> classes) {
        this.classes = classes;
    }

    /**
     * Generates the offsets for methods and attributes of all the classes
     * within this generator.
     * This method must be called before calling any of the getters.
     */
    public void generateOffsets(){
        Set<ClassSymbol> visited = new HashSet<>();
        Map<ClassSymbol, Integer> nextAttrOffset = new HashMap<>();

        for (ClassSymbol clazz : classes) {
            if (visited.contains(clazz)) continue;
            generateOffsetsOf(clazz, visited, nextAttrOffset);
        }
    }

    /**
     * Returns the offset of the given {@link MethodSymbol}.
     * Offsets must have been generated before calling this method.
     *
     * @see #generateOffsets()
     * @param method the method which offset will be returned
     * @return the offset of the given method
     */
    public int getMethodOffset(MethodSymbol method) {
        return methodsOffsets.get(label(method));
    }

    /**
     * Returns the offset of the given {@link AttributeSymbol}.
     * Offsets must have been generated before calling this method.
     *
     * @see #generateOffsets()
     * @param attribute the attribute which offset will be returned
     * @return the offset of the given attribute
     */
    public int getAttributeOffset(AttributeSymbol attribute) {
        return attrOffsets.get(label(attribute));
    }

    private void generateOffsetsOf(ClassSymbol clazz, Set<ClassSymbol> visited, Map<ClassSymbol, Integer> nextAttrOffset) {
        Optional<ClassSymbol> parent = clazz.getParentSymbol();
        parent.ifPresent(classSymbol -> generateOffsetsOf(classSymbol, visited, nextAttrOffset));

        int methodOffset = getInitialMethodOffset(parent.orElse(null));
        int attrOffset = getInitialAttributeOffset(nextAttrOffset, parent.orElse(null));
        generateMethodsOffsets(clazz, methodOffset);
        generateAttributesOffsets(clazz, attrOffset);
        visited.add(clazz);
    }

    /**
     * Returns the offset at which children of the given class must start generating offsets for
     * their own attributes
     */
    private int getInitialAttributeOffset(Map<ClassSymbol, Integer> nextAttrOffset, ClassSymbol parent) {
        return nextAttrOffset.getOrDefault(parent, 0);
    }

    /**
     * Returns the offset at which children of the given class must start generating offsets for
     * their own methods. Methods that overwrite any of the methods of the given class must have
     * the same offset.
     */
    private int getInitialMethodOffset(ClassSymbol parent) {
        return parent != null
                ? (int) parent.getAllMethods().values().stream().filter(MethodSymbol::isDynamic).count()
                : 0;
    }

    private void generateAttributesOffsets(ClassSymbol clazz, int attrOffset) {
        for (AttributeSymbol attr : clazz.getAttributes()) {
            attrOffsets.put(label(attr), attrOffset++);
        }
    }

    private void generateMethodsOffsets(ClassSymbol clazz, int methodOffset) {
        List<MethodSymbol> inheritedMethods = clazz.getInheritMethods().stream().filter(MethodSymbol::isDynamic).collect(Collectors.toList());
        for (MethodSymbol method : clazz.getMethods()) {
            if (inheritedMethods.contains(method)){
                MethodSymbol overwrite = inheritedMethods.get(inheritedMethods.indexOf(method));
                int overwriteOffset = methodsOffsets.get(label(overwrite));
                methodsOffsets.put(label(method), overwriteOffset);
            } else {
                methodsOffsets.put(label(method), methodOffset++);
            }
        }
    }

}
