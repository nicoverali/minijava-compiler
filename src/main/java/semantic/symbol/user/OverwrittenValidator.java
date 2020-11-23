package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.InterfaceSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.Symbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class OverwrittenValidator {

    public static void validateMethods(Map<String, MethodSymbol> inherit, Map<String, MethodSymbol> own){
        for (Map.Entry<String, MethodSymbol> entry : own.entrySet()){
            MethodSymbol overwritten = inherit.get(entry.getKey());
            if (overwritten != null && !overwritten.equals(entry.getValue())){
                throw new SemanticException("Se sobreescribe el metodo pero no se respeta el encabezado", entry.getValue().getNameAttribute());
            }
        }
    }

    public static void validateImplementation(Collection<ReferenceType> interfaces, Map<String, MethodSymbol> methods, NameAttribute className){
        Map<String, MethodSymbol> toImplement = new HashMap<>();
        SymbolTable ST = SymbolTable.getInstance();
        for (ReferenceType ref : interfaces){
            InterfaceSymbol inter = ST.getInterface(ref).get();
            for (MethodSymbol inherit : inter.inheritMethods().values()){
                inherit = ref.getGeneric().map(GenericityAttribute::getValue).map(inherit::instantiate).orElse(inherit);
                MethodSymbol overwritten = toImplement.get(inherit.getName());
                if (overwritten != null && !overwritten.equals(inherit)){
                    throw new SemanticException("La clase extiende dos interfaces cuyos metodos colisionan", className);
                }
                toImplement.put(inherit.getName(), inherit);
            }
        }

        for (MethodSymbol theirs : toImplement.values()){
            MethodSymbol ours = methods.get(theirs.getName());
            if (ours == null){
                throw new SemanticException("La clase no implementa el metodo "+theirs.getName(), className);
            } else if (!ours.equals(theirs)){
                throw new SemanticException("Se implementa el metodo pero no se respeta el encabezado", ours.getNameAttribute());
            }
        }
    }

}
