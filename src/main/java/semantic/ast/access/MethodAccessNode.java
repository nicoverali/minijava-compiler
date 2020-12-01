package semantic.ast.access;

import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.access.chain.ChainNode;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.MethodSymbol;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MethodAccessNode extends BaseAccessNode {

    private final NameAttribute name;
    private final List<ExpressionNode> params;

    public MethodAccessNode(NameAttribute name, List<ExpressionNode> params) {
        super();
        this.name = name;
        this.params = params;
    }

    public MethodAccessNode(NameAttribute name, List<ExpressionNode> params, ChainNode chain) {
        super(chain);
        this.name = name;
        this.params = params;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We'll assume this has already been validated
    @Override
    public Type getType(Scope scope) {
        Type thisType = scope.findMethod(name).get().getReturnType();
        if (hasChainedAccess()){
            scope.setLeftChainType(thisType);
            return chain.getType(scope);
        }
        return thisType;
    }

    @Override
    public AccessNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics(container)){
            List<ExpressionNode> params = this.params.stream().map(exp -> exp.instantiate(container, newType)).collect(Collectors.toList());
            if (hasChainedAccess()){
                return new MethodAccessNode(name, params, chain.instantiate(container, newType));
            }
            else {
                return new MethodAccessNode(name, params);
            }
        }
        return this;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        Optional<Boolean> expGen = params.stream().map(expressionNode -> expressionNode.hasGenerics(container))
                                                    .filter(Boolean::booleanValue).findFirst();
        return super.hasGenerics(container) || expGen.isPresent();
    }

    @Override
    public void validate(Scope scope) {
        Optional<MethodSymbol> method = scope.findMethod(name);
        if (!method.isPresent()){
            throw new SemanticException("El metodo no fue declarado", name);
        }
        ParamsChecker.validateParams(params, method.get().getParameters(), scope, name);
        if (hasChainedAccess()){
            scope.setLeftChainType(method.get().getReturnType());
            chain.validate(scope);
        }
    }

}
