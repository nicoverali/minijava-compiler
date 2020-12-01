package semantic.ast.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
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

public class ChainedMethodNode extends BaseChainNode {

    private final SymbolTable ST = SymbolTable.getInstance();

    private final NameAttribute name;
    private final List<ExpressionNode> params;

    public ChainedMethodNode(Token dotToken, NameAttribute name, List<ExpressionNode> params) {
        super(dotToken);
        this.name = name;
        this.params = params;
    }

    public ChainedMethodNode(ChainNode chain, Token dotToken, NameAttribute name, List<ExpressionNode> params) {
        super(chain, dotToken);
        this.name = name;
        this.params = params;
    }

    @Override
    public boolean hasGenerics() {
        Optional<Boolean> expGen = params.stream().map(ExpressionNode::hasGenerics)
                .filter(Boolean::booleanValue).findFirst();
        return super.hasGenerics() || expGen.isPresent();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We'll assume that the type exists because was checked previously
    @Override
    protected void validate(ReferenceType leftType, Scope scope) {
        TopLevelSymbol symbol = ST.getTopLevelSymbol(leftType).get();
        Optional<MethodSymbol> method = symbol.getMethod(false, name);
        if (!method.isPresent()){
            throw new SemanticException("No se pudo encontrar el metodo", name);
        }
        ParamsChecker.validateParams(params, method.get().getParameters(), scope, name);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // We'll assume that this has already been validated
    @Override
    protected Type getTypeFrom(ReferenceType leftChainType, Scope scope) {
        return ST.getTopLevelSymbol(leftChainType).get().getMethod(false, name).get().getReturnType();
    }

    @Override
    public ChainNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics()){
            List<ExpressionNode> params = this.params.stream().map(exp -> exp.instantiate(container, newType)).collect(Collectors.toList());
            if (hasChainedAccess()){
                return new ChainedMethodNode(chain.instantiate(container, newType), dotToken, name, params);
            }
            return new ChainedMethodNode(dotToken, name, params);
        }
        return this;
    }
}
