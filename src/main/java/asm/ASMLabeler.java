package asm;

import semantic.symbol.AttributeSymbol;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.attribute.type.Type;

import java.util.stream.Collectors;

public class ASMLabeler {

    public static String label(AttributeSymbol attr) {
        String container = attr.getContainer() != null ? attr.getContainer().getName() : "NONE";
        return String.format("%s@%s", attr.getName(), container);
    }

    public static String label(MethodSymbol method) {
        String params = method.getParametersTypes().stream().map(Type::toString).collect(Collectors.joining("$"));
        String container = method.getContainer() != null ? method.getContainer().getName() : "NONE";
        return String.format("%s$%s@%s", method.getName(), params, container);
    }

    public static String label(ConstructorSymbol constructor) {
        String params = constructor.getParametersTypes().stream().map(Type::toString).collect(Collectors.joining("$"));
        String clazz = constructor.getContainer() != null ? constructor.getContainer().getName() : "NONE";
        return String.format("constructor$%s@%s", params, clazz);
    }

}
