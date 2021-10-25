package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.SymbolTable;
import util.map.HashMultimap;
import util.map.Multimap;

import java.util.HashMap;
import java.util.Map;

public class InheritHelper {

    private static final SymbolTable ST = SymbolTable.getInstance();

    public static Multimap<String, MethodSymbol> inheritMethods(ClassSymbol symbol){
        Multimap<String, MethodSymbol> methods = new HashMultimap<>();

        symbol.getParentClass()
            .flatMap(ST::getClass)
            .ifPresent(parentSymbol -> {
                for (MethodSymbol inherit : parentSymbol.getAllMethods().values()) {
                    if (isValidOverload(inherit, methods)) {
                        methods.put(inherit.getName(), inherit);
                    } else {
                        throw new SemanticException("Metodo duplicado", inherit);
                    }
                }
        });

        return methods;
    }

    public static boolean isValidOverload(MethodSymbol method, Multimap<String, MethodSymbol> currentMethods){
        return currentMethods.values().stream().allMatch(m -> m.isValidOverload(method));
    }

    public static Map<String, AttributeSymbol> inheritAttributes(ClassSymbol symbol){
        Map<String, AttributeSymbol> attributes = new HashMap<>();

        symbol.getParentClass()
            .flatMap(ST::getClass)
            .ifPresent(parentSymbol -> {
                for (AttributeSymbol inherit : parentSymbol.getAllAttributes().values()) {
                    attributes.put(inherit.getName(), inherit);
                }
        });

        return attributes;
    }

}
