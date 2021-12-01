package semantic.ast.expression.access;

import asm.ASMWriter;
import lexical.Token;
import semantic.SemanticException;
import semantic.Variable;
import semantic.ast.asm.ASMContext;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.symbol.AttributeSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;
import java.util.regex.Pattern;

public class VarAccessNode extends BaseAccessNode implements VariableAccess {

    private final NameAttribute name;

    private Variable variable;
    private Side side = Side.RIGHT;

    public VarAccessNode(NameAttribute name) {
        this.name = name;
    }

    public VarAccessNode(NameAttribute name, ChainNode chain) {
        super(chain);
        this.name = name;
    }

    @Override
    public Token toToken() {
        return name.getToken();
    }

    @Override
    public Type getAccessType() {
        if (variable == null) throw new IllegalStateException("Make sure to validate before getting the type of an expression");
        return variable.getType();
    }

    @Override
    public void validateAccess(Scope scope) {
        // Search this variable in the current scope
        Optional<Variable> var = scope.findVariable(name);
        if (var.isEmpty()){
            throw new SemanticException("Se hace referencia a una variable no declarada o fuera de alcance", name);
        }
        this.variable = var.get();
    }

    @Override
    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public void generateAccess(ASMContext context, ASMWriter writer) {
        // Dynamic attributes need the reference to the class to access
        if (variable instanceof AttributeSymbol && ((AttributeSymbol) variable).isDynamic()) {
            writer.writeln("LOAD 3\t;\tCargar this");
        }

        if (side.equals(Side.LEFT)){
            variable.generateASMStore(context, writer);
        } else {
            variable.generateASMLoad(context, writer);
        }
    }
}
