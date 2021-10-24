package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.MethodSymbol;
import util.map.Multimap;

import java.util.Collection;

public class OverwrittenValidator {

    public static void validateMethods(Multimap<String, MethodSymbol> inherit, Multimap<String, MethodSymbol> own){
        for (var entry : own.entries()){
            Collection<MethodSymbol> inheritMethods = inherit.get(entry.getKey());
            if (inheritMethods.isEmpty()) continue;

            for (var ownMethod : entry.getValue()) {
                if (!isValidOverwrite(inheritMethods, ownMethod) && !isValidOverload(inheritMethods, ownMethod)){
                    throw new SemanticException("Se sobreescribe el metodo pero no se respeta el encabezado", ownMethod);
                }
            }
        }
    }

    private static boolean isValidOverload(Collection<MethodSymbol> inheritMethods, MethodSymbol ownMethod) {
        return inheritMethods.stream().allMatch(m -> m.isValidOverload(ownMethod));
    }

    private static boolean isValidOverwrite(Collection<MethodSymbol> inheritMethods, MethodSymbol ownMethod) {
        return inheritMethods.stream().anyMatch(m -> m.equals(ownMethod));
    }

}
