package semantic.ast.access;

import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.access.chain.ChainNode;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.MethodSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StaticMethodAccessNode extends BaseAccessNode {

    private final SymbolTable ST = SymbolTable.getInstance();

    private final ReferenceType containerRef;
    private final NameAttribute name;
    private final List<ExpressionNode> params;

    public StaticMethodAccessNode(ReferenceType containerRef, NameAttribute name, List<ExpressionNode> params) {
        this.containerRef = containerRef;
        this.name = name;
        this.params = params;
    }

    public StaticMethodAccessNode(ChainNode chain, ReferenceType containerRef, NameAttribute name, List<ExpressionNode> params) {
        super(chain);
        this.containerRef = containerRef;
        this.name = name;
        this.params = params;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        Optional<Boolean> expGen = params.stream().map(expressionNode -> expressionNode.hasGenerics(container))
                .filter(Boolean::booleanValue).findFirst();
        return super.hasGenerics(container) || expGen.isPresent();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We'll assume that this has already been validated
    @Override
    public Type getType(Scope scope) {
        Type thisType = ST.getTopLevelSymbol(containerRef).get()
                .getMethods().find(true, name).get()
                .getReturnType();

        if (hasChainedAccess()){
            scope.setLeftChainType(thisType);
            return chain.getType(scope);
        }
        return thisType;
    }

    @Override
    public void validate(Scope scope) {
        containerRef.validate(ST, scope.getClassContainer()); // Validate reference
        Optional<MethodSymbol> method = ST.getTopLevelSymbol(containerRef).flatMap(sym -> sym.getMethods().find(true, name));
        if (!method.isPresent()){
            throw new SemanticException("No se pudo encontrar el metodo", name);
        }
        ParamsChecker.validateParams(params, method.get().getParameters(), scope, name);
        if (hasChainedAccess()){
            scope.setLeftChainType(method.get().getReturnType());
            chain.validate(scope);
        }
    }

    @Override
    public NameAttribute getName() {
        return name;
    }

    @Override
    public AccessNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics(container)){
            List<ExpressionNode> params = this.params.stream().map(exp -> exp.instantiate(container, newType)).collect(Collectors.toList());
            if (hasChainedAccess()){
                return new StaticMethodAccessNode(chain.instantiate(container, newType), containerRef, name, params);
            }else{
                return new StaticMethodAccessNode(containerRef, name, params);
            }
        }
        return this;
    }
}
