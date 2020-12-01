package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ClassSymbol extends TopLevelSymbol {

    /**
     * Returns the {@link ConstructorSymbol} of this class. If the class does not have a constructor, then
     * this method will return an empty {@link Optional} and should be assume that a default empty constructor
     * is assigned to this class
     *
     * @return an {@link Optional} wrapping the {@link ConstructorSymbol} of this class
     */
    Optional<ConstructorSymbol> getConstructor();

    /**
     * @return a collection of all the {@link AttributeSymbol} of this class
     */
    Collection<AttributeSymbol> getAttributes();

    /**
     * @return a collection of all the {@link MethodSymbol} of this class
     */
    Collection<MethodSymbol> getMethods();

    /**
     * @return an {@link Optional} wrapping a {@link ReferenceType} pointing to the class from which this class extends
     */
    Optional<ReferenceType> getParentClass();

    /**
     * @return a {@link Collection} of {@link ReferenceType} that point to all the {@link InterfaceSymbol}
     * that are implemented by this class
     */
    Collection<ReferenceType> getInterfaces();

    /**
     * Searches for the given attribute within this symbol. The attribute may be owned by the symbol, or inherited.
     * The attribute must be a public attribute, if a private attribute is found, then the it won't be returned.
     *
     *
     * @param isPublic true if the attribute must be public, false if not
     * @param isStatic true if the attribute must be static, false if not
     * @param name the name of the attribute to look for
     * @return an {@link Optional} wrapping the {@link AttributeSymbol}
     */
    Optional<AttributeSymbol> getAttribute(boolean isPublic, boolean isStatic, NameAttribute name);

    /**
     * Searches for the given attribute within this symbol. The attribute may be owned by the symbol, or inherited.
     * The attribute must be a public attribute, if a private attribute is found, then the it won't be returned.
     *
     *
     * @param isPublic true if the attribute must be public, false if not
     * @param isStatic true if the attribute must be static, false if not
     * @param name the name of the attribute to look for
     * @return an {@link Optional} wrapping the {@link AttributeSymbol}
     */
    Optional<AttributeSymbol> getAttribute(boolean isPublic, boolean isStatic, String name);

    /**
     * Searches for the given attribute within this symbol. The attribute may be owned by the symbol, or inherited.
     * This method won't care about the visibility nor staticness of the attribute, so the returned attribute
     * may be public or private
     *
     * @param name the name of the attribute to look for
     * @return an {@link Optional} wrapping the {@link AttributeSymbol}
     */
    Optional<AttributeSymbol> getAttribute(NameAttribute name);

    /**
     * Searches for the given attribute within this symbol. The attribute may be owned by the symbol, or inherited.
     * This method won't care about the visibility nor staticness of the attribute, so the returned attribute
     * may be public or private
     *
     * @param name the name of the attribute to look for
     * @return an {@link Optional} wrapping the {@link AttributeSymbol}
     */
    Optional<AttributeSymbol> getAttribute(String name);

    /**
     * Returns a map containing all of the ancestors {@link AttributeSymbol},
     * plus the attributes of this particular class.
     * <br>
     * Note that if at any point one attribute is overwritten by other, the the highest attribute will be replaced
     * and the most specific one will remain in the map. The returned map is unmodifiable.
     * <br><br>
     * <i>Beware that the result of this method is cached, so calling it again after new attributes are added will
     * cause inconsistencies</i>
     *
     * @return an unmodifiable map from attributes names to the {@link AttributeSymbol} of this class and its ancestors
     * @throws SemanticException if a semantic error is detected while obtaining all the attributes
     */
    Map<String, AttributeSymbol> inheritAttributes() throws SemanticException;

    /**
     * Returns a map containing all of the ancestors {@link MethodSymbol},
     * plus the methods of this particular class.
     * <br>
     * Note that if at any point one method is overwritten by other, the the highest attribute will be replaced
     * and the most specific one will remain in the map. The returned map is unmodifiable.
     * <br><br>
     * <i>Beware that the result of this method is cached, so calling it again after new methods are added will
     * cause inconsistencies</i>
     *
     * @return an unmodifiable map from method names to the {@link MethodSymbol} of this class and its ancestors
     * @throws SemanticException if a semantic error is detected while obtaining all the methods
     */
    Map<String, MethodSymbol> inheritMethods() throws SemanticException;
}
