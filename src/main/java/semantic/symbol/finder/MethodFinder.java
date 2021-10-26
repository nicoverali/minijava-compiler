package semantic.symbol.finder;

import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;
import util.map.Multimap;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is a helper class to retrieve {@link MethodSymbol} according to different conditions.
 */
public class MethodFinder {

    private final Multimap<String, MethodSymbol> methods;

    public MethodFinder(Multimap<String, MethodSymbol> methods) {
        this.methods = methods;
    }

    public MethodFinder(ClassSymbol container) {
        this.methods = container.getAllMethods();
    }

    /**
     * Searches for {@link MethodSymbol} that match the given name.
     *
     * @param name name of the method
     * @return a {@link Collection} of all the {@link MethodSymbol} that matches the given conditions
     */
    public Collection<MethodSymbol> find(NameAttribute name){
        return methods.get(name.getValue());
    }

    /**
     * Searches for {@link MethodSymbol} that match the given return type and name.
     *
     * @param returnType return type of the method
     * @param name name of the method
     * @return a {@link Collection} of all the {@link MethodSymbol} that matches the given conditions
     */
    public Collection<MethodSymbol> find(Type returnType, NameAttribute name){
        return methods.get(name.getValue())
                .stream()
                .filter(m -> m.getReturnType().equals(returnType))
                .collect(Collectors.toList());
    }

    /**
     * Searches for {@link MethodSymbol} that match the given staticness and name.
     *
     * @param isStatic whether the method is static or not
     * @param name name of the method
     * @return a {@link Collection} of all the {@link MethodSymbol} that matches the given conditions
     */
    public Collection<MethodSymbol> find(IsStaticAttribute isStatic, NameAttribute name){
        return methods.get(name.getValue())
                .stream()
                .filter(m -> m.getStaticAttribute().equals(isStatic))
                .collect(Collectors.toList());
    }

    /**
     * Searches for {@link MethodSymbol} that match the given staticness, return type and name.
     *
     * @param isStatic whether the method is static or not
     * @param returnType the return type of the method
     * @param name name of the method
     * @return a {@link Collection} of all the {@link MethodSymbol} that matches the given conditions
     */
    public Collection<MethodSymbol> find(IsStaticAttribute isStatic, Type returnType, NameAttribute name){
        return methods.get(name.getValue())
                .stream()
                .filter(m -> m.getStaticAttribute().equals(isStatic))
                .filter(m -> m.getReturnType().equals(returnType))
                .collect(Collectors.toList());
    }

    /**
     * Searches for a {@link MethodSymbol} that matches the given name and parameters list.
     * If it exists it will be unique since having multiple methods that match would be a semantic error.
     *
     * @param name name of the method
     * @param parameters an ordered list of the types of parameters of the method
     * @return an {@link Optional} wrapping the {@link MethodSymbol} that matches all conditions
     */
    public Optional<MethodSymbol> find(NameAttribute name, List<Type> parameters){
        List<MethodSymbol> result = methods.get(name.getValue())
                .stream()
                .filter(m -> m.getParametersTypes().equals(parameters))
                .collect(Collectors.toList());

        if (result.size() > 1) throw new SemanticException("Theres is more than one method with the same name and arguments", result.get(1));
        return result.stream().findFirst();
    }

    /**
     * Searches for a {@link MethodSymbol} that matches the given staticness, name and parameters list.
     * If it exists it will be unique since having multiple methods that match would be a semantic error.
     *
     * @param isStatic whether the method is static or not
     * @param name name of the method
     * @param parameters an ordered list of the types of parameters of the method
     * @return an {@link Optional} wrapping the {@link MethodSymbol} that matches all conditions
     */
    public Optional<MethodSymbol> find(IsStaticAttribute isStatic, NameAttribute name, List<Type> parameters){
        List<MethodSymbol> result = methods.get(name.getValue())
                .stream()
                .filter(m -> m.getParametersTypes().equals(parameters))
                .filter(m -> m.getStaticAttribute().equals(isStatic))
                .collect(Collectors.toList());

        if (result.size() > 1) throw new SemanticException("Theres is more than one method with the same name and arguments", result.get(1));
        return result.stream().findFirst();
    }

}