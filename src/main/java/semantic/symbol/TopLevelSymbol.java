package semantic.symbol;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface TopLevelSymbol extends Symbol {

    /**
     * @return the {@link Token} associated with the {@link NameAttribute} of this symbol if any
     */
    Token getNameToken();

    /**
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of the symbol
     */
    Optional<GenericityAttribute> getGeneric();

    /**
     * @return a {@link Collection} of all the {@link ReferenceType} that point to the parents of this symbol
     */
    Collection<ReferenceType> getParents();

    /**
     * @return a {@link MethodContainer} with all the {@link MethodSymbol} of this class
     */
    MethodContainer getMethods();

    /**
     * Returns a {@link Map} of all the {@link MethodSymbol} that a sub-symbol of this symbol will inherit.
     * The key of the map is the name of each method.
     * If this symbol is generic, then its generic methods won't be instantiated, and will be return as generic methods.
     *
     * @return a {@link Map} of all the {@link MethodSymbol} inherit by a sub-class of this symbol
     */
    Map<String, MethodSymbol> inheritMethods();

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
