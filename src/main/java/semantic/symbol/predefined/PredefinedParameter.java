package semantic.symbol.predefined;

import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredefinedParameter that = (PredefinedParameter) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
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
    public String toString() {
        return String.format("%s %s", type, name);
    }
}
