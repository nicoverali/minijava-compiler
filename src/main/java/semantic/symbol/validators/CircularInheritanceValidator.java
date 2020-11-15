package semantic.symbol.validators;

import lexical.Token;
import semantic.CircularInheritanceException;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;
import java.util.stream.Collectors;

public class CircularInheritanceValidator {

    private static final  Collection<TopLevelSymbol> CHECKED = new HashSet<>();

    public static void validate(TopLevelSymbol symbol){
        validate(symbol, new ArrayList<>());
    }

    private static void validate(TopLevelSymbol symbol, List<TopLevelSymbol> visited) {
        if (CHECKED.contains(symbol)) {
            return;
        } else if (visited.contains(symbol)){
            List<Token> involved = visited.stream().map(TopLevelSymbol::getNameToken).collect(Collectors.toList());
            throw new CircularInheritanceException(symbol.getName()+ " sufre de herencia circular", involved);
        }

        visited.add(symbol);
        symbol.getParents().stream()
                .map(ReferenceType::getValue)
                .map(SymbolTable.getInstance()::getTopLevelSymbol)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(i -> validate(i, new ArrayList<>(visited)));
        CHECKED.add(symbol);
    }

}
