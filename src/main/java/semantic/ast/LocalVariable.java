package semantic.ast;

import semantic.Variable;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

public class LocalVariable implements ASTNode, Variable {

    private final Type type;
    private final NameAttribute name;

    public LocalVariable(Type type, NameAttribute name){
        this.type = type;
        this.name = name;
    }

    @Override
    public Type getType(){
        return type;
    }

    @Override
    public NameAttribute getNameAttribute() {
        return name;
    }

    @Override
    public void validate(Scope scope) {
        type.validate(SymbolTable.getInstance(), scope.getTopContainer());
    }

    @Override
    public LocalVariable instantiate(TopLevelSymbol container, String newType){
        if (type instanceof ReferenceType && ((ReferenceType) type).hasGeneric()){
            return new LocalVariable(((ReferenceType) type).instantiate(container, newType), name);
        }
        return this;
    }

    @Override
    public boolean hasGenerics() {
        return type instanceof ReferenceType && ((ReferenceType) type).hasGeneric();
    }

}
