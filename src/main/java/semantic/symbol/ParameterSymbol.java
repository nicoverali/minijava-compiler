package semantic.symbol;

import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public class ParameterSymbol {

    private final NameAttribute name;
    private final Type type;

    public ParameterSymbol(Type type, NameAttribute name) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return the {@link NameAttribute} of this parameter
     */
    public NameAttribute getName() {
        return name;
    }

    /**
     * @return the {@link Type} associated with this parameter
     */
    public Type getType() {
        return type;
    }
}
