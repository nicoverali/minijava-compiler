package semantic.symbol;

import lexical.Token;
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
     * Returns a {@link Map} of all the {@link MethodSymbol} that a sub-symbol of this symbol will inherit.
     * The key of the map is the name of each method.
     * If this symbol is generic, then its generic methods won't be instantiated, and will be return as generic methods.
     *
     * @return a {@link Map} of all the {@link MethodSymbol} inherit by a sub-class of this symbol
     */
    Map<String, MethodSymbol> inheritMethods();

}
