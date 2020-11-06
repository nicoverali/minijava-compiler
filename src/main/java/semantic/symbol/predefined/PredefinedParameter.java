package semantic.symbol.predefined;

import semantic.SemanticException;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

/**
 * A predefined parameter is part of a {@link PredefinedMethod}.
 * It determines the {@link Type} and name of one of its parameters.
 */
public class PredefinedParameter implements ParameterSymbol {

    private final Type type;
    private final NameAttribute name;

    public PredefinedParameter(Type type, String name) {
        this.type = type;
        this.name = NameAttribute.predefined(name);
    }

    @Override
    public NameAttribute getNameAttribute() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    /**
     * @return the name of the predefined parameter as a {@link String}
     */
    public String getName() {
        return name.getValue();
    }

    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        // Unnecessary
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {
        // Do nothing
    }
}
