package semantic.symbol;

import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public interface ParameterSymbol extends InstantiableSymbol<ParameterSymbol> {

    /**
     * @return the {@link Type} associated with this parameter
     */
    Type getType();
}
