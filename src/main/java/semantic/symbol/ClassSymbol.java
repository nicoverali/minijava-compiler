package semantic.symbol;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import util.map.HashSetMultimap;
import util.map.Multimap;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ClassSymbol extends Symbol {

    /**
     * @return a collection of all the {@link AttributeSymbol} of this class
     */
    Collection<AttributeSymbol> getAttributes();

    /**
     * Returns a {@link Collection} of the {@link AttributeSymbol} inherited by this class.
     * This method will only work if the class has already been consolidated.
     *
     * @see #consolidate()
     * @return a collection of all the {@link AttributeSymbol} that are inherited by this class
     * @throws IllegalStateException if the class has not been consolidated yet
     */
    Collection<AttributeSymbol> getInheritAttributes() throws IllegalStateException;

    /**
     * @return a collection of all the {@link MethodSymbol} of this class
     */
    Collection<MethodSymbol> getMethods();

    /**
     * Returns a {@link Collection} of the {@link MethodSymbol} inherited by this class.
     * This method will only work if the class has already been consolidated.
     *
     * @see #consolidate()
     * @return a collection of all the {@link MethodSymbol} that are inherited by this class
     * @throws IllegalStateException if the class has not been consolidated yet
     */
    Collection<MethodSymbol> getInheritMethods() throws IllegalStateException;

    /**
     * @return an {@link Optional} wrapping a {@link ReferenceType} pointing to the class from which this class extends
     */
    Optional<ReferenceType> getParentClass();

    /**
     * Returns all the {@link ConstructorSymbol} of this class. If the user did not set any constructor
     * for this class, then at least a default constructor will be returned.
     *
     * Is safe to assume that the collection of constructor will never be empty
     *
     * @return a {@link Collection} of {@link ConstructorSymbol} of this class
     */
    Collection<ConstructorSymbol> getConstructors();

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
     * @return a {@link HashSetMultimap} from method names to the {@link MethodSymbol} of this class and its ancestors
     * @throws SemanticException if a semantic error is detected while obtaining all the methods
     */
    Multimap<String, MethodSymbol> getAllMethods() throws SemanticException;

    /**
     * @return the {@link Token} associated with the {@link NameAttribute} of this symbol if any
     */
    Token getNameToken();

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

    /**
     * Verifies that all sentences within this class are valid. This check is meant to be the last, so
     * make sure to check declarations and consolidate classes before calling this method.
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this validation
     */
    void checkSentences() throws  SemanticException, IllegalStateException;
}
