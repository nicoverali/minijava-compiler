package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

public class UserParameterSymbol implements ParameterSymbol {

    private final NameAttribute name;
    private final Type type;

    private TopLevelSymbol topSymbol;

    public UserParameterSymbol(Type type, NameAttribute name) {
        this.name = name;
        this.type = type;
    }

    @Override public NameAttribute getNameAttribute() {
        return name;
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override public Type getType() {
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
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        if (topSymbol == null) throw new IllegalStateException("El parametro no forma parte de ningun simbolo de nivel superior.");
        this.type.validate(SymbolTable.getInstance(), topSymbol);
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {

    }

    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        topSymbol = symbol;
    }

    @Override
    public ParameterSymbol instantiate(String newType) {
        if (type instanceof ReferenceType){
            return new UserParameterSymbol(((ReferenceType) type).instantiate(topSymbol, newType), name);
        }
        return this;
    }
}
