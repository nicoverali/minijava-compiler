package semantic.symbol;

import semantic.Variable;
import semantic.symbol.attribute.type.Type;

public interface ParameterSymbol extends InnerClassSymbol, Variable {

    /**
     * @return the {@link Type} associated with this parameter
     */
    Type getType();
}
