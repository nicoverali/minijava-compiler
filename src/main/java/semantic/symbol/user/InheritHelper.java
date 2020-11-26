package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.*;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class InheritHelper {

    /*
        This method won't check if the parent of the given symbol is either a class or interface, so
        remember to check that inheritance is correct before calling this method.
     */
    public static Map<String, MethodSymbol> inheritMethods(TopLevelSymbol symbol){
        SymbolTable ST = SymbolTable.getInstance();
        Map<String, MethodSymbol> methods = new HashMap<>();
        for (ReferenceType parentRef : symbol.getParents()){
            TopLevelSymbol parentSymbol = ST.getTopLevelSymbol(parentRef).get();

            for (MethodSymbol inherit : parentSymbol.inheritMethods().values()){
                inherit = instatiateMember(inherit, parentRef, parentSymbol);
                MethodSymbol overwritten = methods.get(inherit.getName());
                if (overwritten != null && !overwritten.equals(inherit)){
                    throw new SemanticException("Se extiende dos interfaces cuyos metodos colisionan", symbol.getNameToken());
                }
                methods.put(inherit.getName(), inherit);
            }

        }

        return methods;
    }


    /*
        This method won't check if the given class does actually have a parent or not, if does not this will simply
        return an empty map
     */
    public static Map<String, AttributeSymbol> inheritAttributes(ClassSymbol symbol){
        Map<String, AttributeSymbol> attributes = new HashMap<>();

        if (symbol.getParentClass().isPresent()){
            ReferenceType parentRef = symbol.getParentClass().get();
            ClassSymbol parent = SymbolTable.getInstance().getClass(parentRef).get();

            for (AttributeSymbol inherit : parent.inheritAttributes().values()){
                inherit = instatiateMember(inherit, parentRef, parent);
                attributes.put(inherit.getName(), inherit);
            }
        }

        return attributes;
    }

    public static <T extends InstantiableSymbol<T>> T instatiateMember(T inherit, ReferenceType parentInstance, TopLevelSymbol parent) {
        Optional<GenericityAttribute> instanceGen = parentInstance.getGeneric();
        if (instanceGen.isPresent()){
            return inherit.instantiate(parent, instanceGen.get().getValue());
        }
        return inherit;
    }

}
