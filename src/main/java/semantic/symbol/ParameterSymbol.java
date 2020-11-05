package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

public class ParameterSymbol implements InnerLevelSymbol {

    private final NameAttribute name;
    private final Type type;

    private TopLevelSymbol topSymbol;

    public ParameterSymbol(Type type, NameAttribute name) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return the {@link NameAttribute} of this parameter
     */
    public NameAttribute getNameAttribute() {
        return name;
    }

    /**
     * @return the {@link Type} associated with this parameter
     */
    public Type getType() {
        return type;
    }

    @Override
    public void consolidate() throws SemanticException {
    }

    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        topSymbol = symbol;
    }
}
