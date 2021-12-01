package semantic.ast.block;

import asm.ASMWriter;
import semantic.Variable;
import semantic.ast.ASTNode;
import semantic.ast.asm.ASMContext;
import semantic.ast.scope.Scope;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Objects;

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
    public void generateASMLoad(ASMContext context, ASMWriter writer) {
        writer.writeln("LOAD %s\t;\tCargar variable local", context.getOffsetOf(this));
    }

    @Override
    public void generateASMStore(ASMContext context, ASMWriter writer) {
        writer.writeln("STORE %s\t;\tGuardar valor de pila en variable local", context.getOffsetOf(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalVariable that = (LocalVariable) o;
        return Objects.equals(type, that.type) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }

    @Override
    public void validate(Scope scope) {
        type.validate(SymbolTable.getInstance());
    }

}
