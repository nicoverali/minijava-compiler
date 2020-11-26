package semantic.symbol;

import lexical.Token;
import semantic.CircularInheritanceException;
import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.type.ReferenceType;
import util.HashMultimap;
import util.Iterables;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("OptionalGetWithoutIsPresent") // We assume that all reference types have been checked
public class SymbolTreeValidator {

    private static final HashMultimap<TopLevelSymbol, ReferenceType> CHECKED = new HashMultimap<>();

    public static void validate(Map<String, ? extends TopLevelSymbol> classes){
        classes.values().forEach(clazz -> validate(clazz, new ArrayList<>()));
    }

    private static Collection<ReferenceType> validate(TopLevelSymbol symbol, List<TopLevelSymbol> visited){
        if (CHECKED.containsKey(symbol)) {
            return CHECKED.get(symbol);
        } else if (visited.contains(symbol)){
            List<Token> involved = visited.stream().map(TopLevelSymbol::getNameToken).collect(Collectors.toList());
            throw new CircularInheritanceException(symbol.getName()+ " sufre de herencia circular", involved);
        }
        visited.add(symbol);

        Collection<ReferenceType> allInterfaces = getInterfacesOf(symbol);
        Collection<ReferenceType> parentInterfaces = symbol.getParents().stream()
                .map(SymbolTable.getInstance()::getTopLevelSymbol)
                .map(i -> validate(i.get(), new ArrayList<>(visited)))
                .flatMap(Collection::stream).collect(Collectors.toList());
        allInterfaces.addAll(parentInterfaces);
        verifyGenericsCollision(allInterfaces, symbol);

        CHECKED.putAll(symbol, allInterfaces);
        return allInterfaces;
    }

    private static Collection<ReferenceType> getInterfacesOf(TopLevelSymbol symbol){
        if (symbol instanceof ClassSymbol){
            return ((ClassSymbol) symbol).getInterfaces();
        }
        return symbol.getParents();
    }

    private static void verifyGenericsCollision(Collection<ReferenceType> allInterfaces, TopLevelSymbol container) throws SemanticException {
        HashMultimap<String, ReferenceType> interfaceMap = new HashMultimap<>();
        allInterfaces.forEach(ref -> interfaceMap.put(ref.getValue(), ref));

        for (String key : interfaceMap.keys()){
            Collection<ReferenceType> refs = interfaceMap.get(key);
            if (refs.stream().distinct().count() > 1){
                ReferenceType first = Iterables.getFirst(refs);
                throw new SemanticException("No se puede instanciar "+first.getValue()+" con dos tipos genericos diferentes", container.getNameToken());
            }
        }
    }


}
