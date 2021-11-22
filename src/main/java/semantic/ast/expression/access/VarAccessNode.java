package semantic.ast.expression.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.Variable;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

public class VarAccessNode extends BaseAccessNode {

    private final NameAttribute name;

    private Variable variable;

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
}
