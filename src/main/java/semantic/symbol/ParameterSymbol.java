package semantic.symbol;

import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public interface ParameterSymbol extends InnerLevelSymbol {
    /**
     * @return the {@link NameAttribute} of this parameter
     */
    NameAttribute getNameAttribute();

    /**
     * @return the {@link Type} associated with this parameter
     */
    Type getType();
}
