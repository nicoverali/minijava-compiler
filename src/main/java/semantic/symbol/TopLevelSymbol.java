package semantic.symbol;

import semantic.symbol.attribute.GenericityAttribute;

import java.util.Optional;

public interface TopLevelSymbol extends Symbol {

    /**
     * @return the name of this symbol as a {@link String}
     */
    String getName();

    /**
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of the symbol
     */
    Optional<GenericityAttribute> getGeneric();

}
