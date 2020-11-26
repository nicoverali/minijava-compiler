package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;

public interface Symbol {

    /**
     * @return the name of this symbol as a {@link String}
     */
    String getName();

    /**
     * @return the {@link NameAttribute} of this symbol
     */
    NameAttribute getNameAttribute();

}
