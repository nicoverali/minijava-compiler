package semantic.symbol;

import semantic.symbol.attribute.NameAttribute;

import java.util.Map;
import java.util.Optional;

/**
 * This is a helper class to retrieve {@link AttributeSymbol} according to different conditions.
 */
public class AttributeContainer {

    private final Map<String, AttributeSymbol> attributes;
    private final Map<String, AttributeSymbol> inheritedAttributes;

    public AttributeContainer(Map<String, AttributeSymbol> attributes, Map<String, AttributeSymbol> inheritedAttributes) {
        this.attributes = attributes;
        this.inheritedAttributes = inheritedAttributes;
    }

    /**
     * Searches for an {@link AttributeSymbol} that matches the condition of name, visibility, and staticness given.
     * If any of this conditions is not given, then it will be ignored and any attribute that matches all the other
     * conditions will be returned.
     *
     * @param name name of the attribute
     * @return an {@link Optional} wrapping the {@link AttributeSymbol} that matches all conditions
     */
    public Optional<AttributeSymbol> find(String name){
        AttributeSymbol attr = attributes.get(name);
        if (attr != null){
            return Optional.of(attr);
        }
        return Optional.ofNullable(inheritedAttributes.get(name));
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
        return find(name.getValue());
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
    public Optional<AttributeSymbol> find(boolean isPublic, boolean isStatic, String name){
        AttributeSymbol attr = attributes.get(name);
        if (attr != null && attr.isPublic().equals(isPublic) && attr.isStatic().equals(isStatic)){
            return Optional.of(attr);
        }
        return Optional.ofNullable(inheritedAttributes.get(name));
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
    public Optional<AttributeSymbol> find(boolean isPublic, boolean isStatic, NameAttribute name){
        return find(isPublic, isStatic, name.getValue());
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
    public Optional<AttributeSymbol> findS(boolean isStatic, String name){
        AttributeSymbol attr = attributes.get(name);
        if (attr != null && attr.isStatic().equals(isStatic)){
            return Optional.of(attr);
        }
        return Optional.ofNullable(inheritedAttributes.get(name));
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
    public Optional<AttributeSymbol> findS(boolean isStatic, NameAttribute name){
        return findS(isStatic, name.getValue());
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
    public Optional<AttributeSymbol> findV(boolean isPublic, String name){
        AttributeSymbol attr = attributes.get(name);
        if (attr != null && attr.isPublic().equals(isPublic)){
            return Optional.of(attr);
        }
        return Optional.ofNullable(inheritedAttributes.get(name));
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
    public Optional<AttributeSymbol> findV(boolean isPublic, NameAttribute name){
        return findV(isPublic, name.getValue());
    }



}
