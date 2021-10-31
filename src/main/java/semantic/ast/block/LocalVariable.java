package semantic.ast.block;

import semantic.Variable;
import semantic.ast.ASTNode;
import semantic.ast.scope.Scope;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
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
        type.validate(SymbolTable.getInstance());
    }

}
