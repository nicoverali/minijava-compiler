package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ClassSymbol extends TopLevelSymbol {

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
    Optional<ReferenceType> getParent();

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
