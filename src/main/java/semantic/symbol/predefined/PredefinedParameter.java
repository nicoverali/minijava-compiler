package semantic.symbol.predefined;

import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.ParameterSymbol;
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ParameterSymbol && equals((ParameterSymbol) obj);
    }

    @Override
    public boolean equals(ParameterSymbol parameter) {
        return this.type.equals(parameter.getType());
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public void checkDeclaration(ClassSymbol container) throws SemanticException, IllegalStateException {
        // Do nothing
    }

    @Override
    public ParameterSymbol instantiate(ClassSymbol container, String newType) {
        return this;
    }
}
