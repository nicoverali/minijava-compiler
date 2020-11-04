package syntactic.entity;

import syntactic.entity.attribute.NameAttribute;
import syntactic.entity.attribute.type.Type;

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
