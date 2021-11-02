package semantic.symbol.finder;

import semantic.symbol.AttributeSymbol;
import semantic.symbol.ClassSymbol;
import semantic.symbol.attribute.IsPublicAttribute;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This is a helper class to retrieve {@link AttributeSymbol} according to different conditions.
 */
public class AttributeFinder {

    private final Map<String, AttributeSymbol> attributes;
    private final Map<String, AttributeSymbol> inheritAttributes;
    private final Map<String, AttributeSymbol> allAtributes;

    public AttributeFinder(ClassSymbol container){
        this.attributes = new HashMap<>();
        this.inheritAttributes = new HashMap<>();
        this.allAtributes = container.getAllAttributes();

        container.getAttributes().forEach(attr -> attributes.put(attr.getName(), attr));
        container.getInheritAttributes().forEach(attr -> inheritAttributes.put(attr.getName(), attr));
    }

    /**
     * Searches for an {@link AttributeSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any attribute that matches all the other
     * conditions will be returned.
     *
     * @param name name of the attribute
     * @return an {@link Optional} wrapping the {@link AttributeSymbol} that matches all conditions
     */
    public Optional<AttributeSymbol> find(NameAttribute name){
        return Optional.ofNullable(attributes.get(name.getValue()))
                .or(() ->
                        Optional.ofNullable(inheritAttributes.get(name.getValue()))
                                .filter(attr -> attr.isPublic().getValue().equals(true))
                );
    }

    /**
     * Searches for an {@link AttributeSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any attribute that matches all the other
     * conditions will be returned.
     *
     * @param isPublic whether the attribute must be public or not
     * @param isStatic whether the attribute must be static or not
     * @param name name of the attribute
     * @return an {@link Optional} wrapping the {@link AttributeSymbol} that matches all conditions
     */
    public Optional<AttributeSymbol> find(IsPublicAttribute isPublic, IsStaticAttribute isStatic, NameAttribute name){
        return Optional.ofNullable(allAtributes.get(name.getValue()))
                .filter(attr -> attr.isPublic().equals(isPublic))
                .filter(attr -> attr.isStatic().equals(isStatic));
    }

    /**
     * Searches for an {@link AttributeSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any attribute that matches all the other
     * conditions will be returned.
     *
     * @param isStatic whether the attribute must be static or not
     * @param name name of the attribute
     * @return an {@link Optional} wrapping the {@link AttributeSymbol} that matches all conditions
     */
    public Optional<AttributeSymbol> find(IsStaticAttribute isStatic, NameAttribute name){
        return Optional.ofNullable(attributes.get(name.getValue()))
                .or(() ->
                        Optional.ofNullable(inheritAttributes.get(name.getValue()))
                                .filter(attr -> attr.isPublic().getValue().equals(true))
                )
                .filter(attr -> attr.isStatic().equals(isStatic));
    }

    /**
     * Searches for an {@link AttributeSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any attribute that matches all the other
     * conditions will be returned.
     *
     * @param isPublic whether the attribute must be public or not
     * @param name name of the attribute
     * @return an {@link Optional} wrapping the {@link AttributeSymbol} that matches all conditions
     */
    public Optional<AttributeSymbol> find(IsPublicAttribute isPublic, NameAttribute name){
        return Optional.ofNullable(allAtributes.get(name.getValue()))
                .filter(attr -> attr.isPublic().equals(isPublic));
    }
}