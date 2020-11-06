package semantic.symbol.attribute;

import lexical.Token;

/**
 * A {@link SymbolAttribute} determines a certain aspect of a Symbol.
 * It contains a value and may or may not contain an associated {@link Token}.
 *
 * If the value is obtained from the source code, then it will have a {@link Token} associated. If not, the value
 * may be a default value or may be given by the compiler in some way, in which case it won't have a {@link Token} associated
 *
 * @param <T> the type of value of this attribute
 */
public interface SymbolAttribute<T> {

    /**
     * @return the {@link Token} associated with this attribute. This value may be null if the value of the attribute
     * was not obtained from the source code
     */

    Token getToken();

    /**
     * @return the value <code>T</code> that determines an aspect of the Symbol which has this attributed associated
     */
    T getValue();

}
