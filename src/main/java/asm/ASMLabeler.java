package asm;

import semantic.symbol.*;
import semantic.symbol.attribute.type.Type;

import java.util.stream.Collectors;

public class ASMLabeler {

    private static int nextLabel = 0;

    public static String label(String prefix) {
        return String.format("l_%s_%s", prefix, nextLabel++);
    }

    public static String label(AttributeSymbol attr) {
        String container = attr.getContainer() != null ? attr.getContainer().getName() : "NONE";
        return String.format("%s@%s", attr.getName(), container);
    }

    public static String label(CallableSymbol callable) {
        String params = callable.getParametersTypes().stream().map(Type::toString).collect(Collectors.joining("$"));
        String clazz = callable.getContainer() != null ? callable.getContainer().getName() : "NONE";

        return String.format("%s$%s@%s", callable.getName(), params, clazz);
    }

    public static String labelVT(ClassSymbol classSymbol) {
        return "VT_"+classSymbol.getName();
    }

}
