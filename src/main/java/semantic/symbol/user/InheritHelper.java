package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.*;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;
import java.util.stream.Collectors;

public class InheritHelper {

    /*
        This method won't check if the parent of the given symbol is either a class or interface, so
        remember to check that inheritance is correct before calling this method.
     */
    public static Map<String, MethodSymbol> inheritMethods(TopLevelSymbol symbol){
        Map<String, MethodSymbol> methods = new HashMap<>();
        List<TopLevelSymbol> parents = symbol.getParents().stream()
                .map(ReferenceType::getValue)
                .map(SymbolTable.getInstance()::getTopLevelSymbol)
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());

        for (TopLevelSymbol parent : parents){
            for (Map.Entry<String, MethodSymbol> entry : parent.inheritMethods().entrySet()){
                MethodSymbol overwritten = methods.get(entry.getKey());
                if (overwritten != null && !overwritten.equals(entry.getValue())){
                    throw new SemanticException("Se extiende dos interfaces cuyos metodos colisionan", symbol.getNameToken());
                }
                methods.put(entry.getKey(), entry.getValue());
            }
        }

        return methods;
    }

    /*
        This method won't check if the given class does actually have a parent or not, if does not this will simply
        return an empty map
     */
    public static Map<String, AttributeSymbol> inheritAttributes(ClassSymbol symbol){
        return symbol.getParent()
                .map(ReferenceType::getValue)
                .flatMap(SymbolTable.getInstance()::getUserClass)
                .map(UserClassSymbol::inheritAttributes)
                .orElse(Collections.emptyMap());
    }

}
