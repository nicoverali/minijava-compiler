package semantic.symbol;

import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;

import java.util.Map;
import java.util.Optional;

/**
 * This is a helper class to retrieve {@link MethodSymbol} according to different conditions.
 */
public class MethodContainer {

    private final Map<String, MethodSymbol> methods;
    private final Map<String, MethodSymbol> inheritedMethods;

    public MethodContainer(Map<String, MethodSymbol> methods, Map<String, MethodSymbol> inheritedMethods) {
        this.methods = methods;
        this.inheritedMethods = inheritedMethods;
    }

    /**
     * Searches for an {@link MethodSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any method that matches all the other
     * conditions will be returned.
     *
     * @param name name of the method
     * @return an {@link Optional} wrapping the {@link MethodSymbol} that matches all conditions
     */
    public Optional<MethodSymbol> find(String name){
        MethodSymbol method = methods.get(name);
        if (method != null){
            return Optional.of(method);
        }
        return Optional.ofNullable(inheritedMethods.get(name));
    }

    /**
     * Searches for an {@link MethodSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any method that matches all the other
     * conditions will be returned.
     *
     * @param name name of the method
     * @return an {@link Optional} wrapping the {@link MethodSymbol} that matches all conditions
     */
    public Optional<MethodSymbol> find(NameAttribute name){
        return find(name.getValue());
    }

    /**
     * Searches for an {@link MethodSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any method that matches all the other
     * conditions will be returned.
     *
     * @param isStatic whether the method must be static or not
     * @param name name of the method
     * @return an {@link Optional} wrapping the {@link MethodSymbol} that matches all conditions
     */
    public Optional<MethodSymbol> find(boolean isStatic, String name){
        MethodSymbol method = methods.get(name);
        if (method != null && method.isStatic().equals(isStatic)){
            return Optional.of(method);
        }
        return Optional.ofNullable(inheritedMethods.get(name));
    }

    /**
     * Searches for an {@link MethodSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any method that matches all the other
     * conditions will be returned.
     *
     * @param isStatic whether the method must be static or not
     * @param name name of the method
     * @return an {@link Optional} wrapping the {@link MethodSymbol} that matches all conditions
     */
    public Optional<MethodSymbol> find(boolean isStatic, NameAttribute name){
        return find(isStatic, name.getValue());
    }
    
}
