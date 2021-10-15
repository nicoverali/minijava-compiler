package semantic.symbol;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ClassSymbol extends Symbol {

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
    Map<String, AttributeSymbol> getAllAttributes() throws SemanticException;

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
    Map<String, MethodSymbol> getAllMethods() throws SemanticException;

    /**
     * @return the {@link Token} associated with the {@link NameAttribute} of this symbol if any
     */
    Token getNameToken();

    /**
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of the symbol
     */
    Optional<GenericityAttribute> getGeneric();

    /**
     * Verifies that the symbol declaration is valid. This method should be execute once the {@link SymbolTable}
     * is complete, otherwise it may not work correctly.
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this validation
     */
    void checkDeclaration() throws SemanticException, IllegalStateException;

    /**
     * Makes the symbol fulfill its information related to other symbols, like inherit methods, generics
     * instantiation, etc.
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this consolidation
     */
    void consolidate() throws SemanticException, IllegalStateException;
}
