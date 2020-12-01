package semantic.ast.access;

import semantic.SemanticException;
import semantic.Variable;
import semantic.ast.Scope;
import semantic.ast.access.chain.ChainNode;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

public class VarAccessNode extends BaseAccessNode {

    private final NameAttribute name;

    public VarAccessNode(NameAttribute name) {
        this.name = name;
    }

    public VarAccessNode(NameAttribute name, ChainNode chain) {
        super(chain);
        this.name = name;
    }

    @Override
    public AccessNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics(container)){
            return new VarAccessNode(name, chain.instantiate(container, newType));
        }
        return this;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We'll assume that this has been validated
    @Override
    public Type getType(Scope scope) {
        Type thisType = scope.findVariable(this.name).get().getType();
        if (hasChainedAccess()){
            scope.setLeftChainType(thisType);
            return chain.getType(scope);
        }
        return thisType;
    }

    @Override
    public void validate(Scope scope) {
        Optional<Variable> var = scope.findVariable(this.name);
        if (!var.isPresent()){
            throw new SemanticException("Se hace referencia a una variable no declarada", name);
        }
        if (hasChainedAccess()){
            scope.setLeftChainType(var.get().getType());
            chain.validate(scope);
        }
    }
}
