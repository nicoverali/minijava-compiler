package semantic.symbol;

import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public interface ParameterSymbol extends InnerLevelSymbol {

    /**
     * @return the {@link Type} associated with this parameter
     */
    Type getType();

    /**
     * @return true if the given {@link ParameterSymbol} has the same {@link Type} as this one
     */
    boolean equals(ParameterSymbol parameter);
}
