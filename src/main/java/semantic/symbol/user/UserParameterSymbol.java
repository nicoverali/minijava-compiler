package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.Objects;

public class UserParameterSymbol implements ParameterSymbol {

    private final NameAttribute name;
    private final Type type;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserParameterSymbol that = (UserParameterSymbol) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public void checkDeclaration(ClassSymbol container) throws SemanticException, IllegalStateException {
        this.type.validate(SymbolTable.getInstance(), container);
    }

    @Override
    public ParameterSymbol instantiate(ClassSymbol container, String newType) {
        if (type instanceof ReferenceType){
            return new UserParameterSymbol(((ReferenceType) type).instantiate(container, newType), name);
        }
        return this;
    }
}
