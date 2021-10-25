package semantic.symbol;

import semantic.symbol.attribute.type.Type;

public interface ParameterSymbol extends InnerClassSymbol {

    /**
     * @return the {@link Type} associated with this parameter
     */
    Type getType();
}
