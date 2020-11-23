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
        for (ReferenceType parent : symbol.getParents()){

            TopLevelSymbol parentSymbol = ST.getTopLevelSymbol(parent).get();
            for (MethodSymbol inherit : parentSymbol.inheritMethods().values()){
                inherit = parent.getGeneric().map(GenericityAttribute::getValue).map(inherit::instantiate).orElse(inherit);
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
            Optional<String> parentGen = symbol.getParentClass().flatMap(ReferenceType::getGeneric).map(GenericityAttribute::getValue);
            ClassSymbol parent = SymbolTable.getInstance().getClass(symbol.getParentClass().get()).get();

            for (AttributeSymbol inherit : parent.inheritAttributes().values()){
                inherit = parentGen.map(inherit::instantiate).orElse(inherit);
                attributes.put(inherit.getName(), inherit);
            }
        }

        return attributes;
    }

}
