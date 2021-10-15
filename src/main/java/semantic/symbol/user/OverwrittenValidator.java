package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.MethodSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;

public class OverwrittenValidator {

    public static void validateMethods(Map<String, MethodSymbol> inherit, Map<String, MethodSymbol> own){
        for (Map.Entry<String, MethodSymbol> entry : own.entrySet()){
            MethodSymbol overwritten = inherit.get(entry.getKey());
            if (overwritten != null && !overwritten.equals(entry.getValue())){
                throw new SemanticException("Se sobreescribe el metodo pero no se respeta el encabezado", entry.getValue().getNameAttribute());
            }
        }
    }

}
