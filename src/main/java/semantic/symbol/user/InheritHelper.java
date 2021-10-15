package semantic.symbol.user;

import semantic.symbol.*;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InheritHelper {

    private static final SymbolTable ST = SymbolTable.getInstance();

    public static Map<String, MethodSymbol> inheritMethods(ClassSymbol symbol){
        Map<String, MethodSymbol> methods = new HashMap<>();

        symbol.getParentClass().ifPresent(parentRef ->
            ST.getClass(parentRef).ifPresent(parentSymbol -> {
                for (MethodSymbol inherit : parentSymbol.getAllMethods().values()){
                    inherit = instantiateMember(inherit, parentRef, parentSymbol);
                    methods.put(inherit.getName(), inherit);
                }
            }
        ));

        return methods;
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
