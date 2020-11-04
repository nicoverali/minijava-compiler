package semantic.symbol.predefined;

import semantic.symbol.attribute.type.Type;

/**
 * A predefined parameter is part of a {@link PredefinedMethod}.
 * It determines the {@link Type} and name of one of its parameters.
 */
public class PredefinedParameter {

    private final Type type;
    private final String name;

    public PredefinedParameter(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * @return the {@link Type} of the predefined parameter
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the name of the predefined parameter as a {@link String}
     */
    public String getName() {
        return name;
    }
}
