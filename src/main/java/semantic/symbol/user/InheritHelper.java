package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.*;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import util.map.HashMultimap;
import util.map.HashSetMultimap;
import util.map.Multimap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class InheritHelper {

    private static final SymbolTable ST = SymbolTable.getInstance();

    public static Multimap<String, MethodSymbol> inheritMethods(ClassSymbol symbol){
        Multimap<String, MethodSymbol> methods = new HashMultimap<>();

        symbol.getParentClass().ifPresent(parentRef ->
            ST.getClass(parentRef).ifPresent(parentSymbol -> {
                for (MethodSymbol inherit : parentSymbol.getAllMethods().values()){
                    inherit = instantiateMember(inherit, parentRef, parentSymbol);
                    if (isValidOverload(inherit, methods)){
                        methods.put(inherit.getName(), inherit);
                    } else {
                        throw new SemanticException("Metodo duplicado", inherit);
                    }
                }
            }
        ));

        return methods;
    }

    public static boolean isValidOverload(MethodSymbol method, Multimap<String, MethodSymbol> currentMethods){
        return currentMethods.values().stream().allMatch(m -> m.isValidOverload(method));
    }

    public static Map<String, AttributeSymbol> inheritAttributes(ClassSymbol symbol){
        Map<String, AttributeSymbol> attributes = new HashMap<>();

        symbol.getParentClass().ifPresent(parentRef ->
            ST.getClass(parentRef).ifPresent(parentSymbol -> {
                for (AttributeSymbol inherit : parentSymbol.getAllAttributes().values()){
                    inherit = instantiateMember(inherit, parentRef, parentSymbol);
                    attributes.put(inherit.getName(), inherit);
                }
            })
        );

        return attributes;
    }

    // TODO Check that this instantiation is ok if we implement Genericity
    public static <T extends InstantiableSymbol<T>> T instantiateMember(T inherit, ReferenceType parentInstance, ClassSymbol parent) {
        Optional<GenericityAttribute> instanceGen = parentInstance.getGeneric();
        if (instanceGen.isPresent()){
            return inherit.instantiate(parent, instanceGen.get().getValue());
        }
        return inherit;
    }

}
