package semantic.symbol;

import lexical.Token;
import semantic.CircularInheritanceException;
import semantic.MainMethodException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SymbolTreeValidator {

    private static final String MAIN_METHOD_NAME = "main";

    public static void checkCircularInheritance(Map<String, ? extends ClassSymbol> classes){
        classes.values().forEach(clazz -> checkCircularInheritance(clazz, new ArrayList<>()));
    }

    private static void checkCircularInheritance(ClassSymbol classSymbol, List<ClassSymbol> visited){
        if (visited.contains(classSymbol)){
            List<Token> involved = visited.stream().map(ClassSymbol::getNameToken).collect(Collectors.toList());
            throw new CircularInheritanceException(classSymbol.getName()+ " sufre de herencia circular", involved);
        }
        visited.add(classSymbol);
        // If we have a parent, validate circular inheritance with it
        classSymbol.getParentSymbol()
                .ifPresent(parentSymbol -> checkCircularInheritance(parentSymbol, visited));
    }

    public static MethodSymbol checkMainMethod(Map<String, ? extends ClassSymbol> classes){
        Stream<MethodSymbol> allMethods = classes.values().stream()
                .map(ClassSymbol::getMethods)
                .flatMap(Collection::stream);

        List<MethodSymbol> mainMethods = allMethods
                .filter(method -> method.getName().equals(MAIN_METHOD_NAME))
                .filter(MethodSymbol::isStatic)
                .filter(Predicate.not(MethodSymbol::hasParameters))
                .collect(Collectors.toList());

        if (mainMethods.size() > 1){
            throw new MainMethodException("Solo puede haber un unico metodo estatico main sin parametros", mainMethods.get(1));
        } else if (mainMethods.size() == 0){
            throw new MainMethodException("Debe haber una clase con un metodo estatico main sin parametros");
        }
        return mainMethods.get(0);
    }

}
