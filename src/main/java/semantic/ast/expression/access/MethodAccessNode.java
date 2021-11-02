package semantic.ast.expression.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.MethodSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MethodAccessNode extends BaseAccessNode {

    private final NameAttribute name;
    private final List<ExpressionNode> paramsExps;

    private MethodSymbol methodSymbol;

    public MethodAccessNode(NameAttribute name, List<ExpressionNode> paramsExps) {
        super();
        this.name = name;
        this.paramsExps = paramsExps;
    }

    public MethodAccessNode(NameAttribute name, List<ExpressionNode> paramsExps, ChainNode chain) {
        super(chain);
        this.name = name;
        this.paramsExps = paramsExps;
    }

    @Override
    public Type getType() {
        if (methodSymbol == null) throw new IllegalStateException("Make sure to validate before getting the type of an expression");
        Type thisType = methodSymbol.getReturnType();
        return thisTypeOrChainType(thisType);
    }

    @Override
    public Token toToken() {
        return name.getToken();
    }

    @Override
    public void validateAccess(Scope scope) {
        // Validate parameters and get types of expressions
        paramsExps.forEach(exp -> exp.validate(scope));
        List<Type> paramsTypes = paramsExps.stream().map(ExpressionNode::getType).collect(Collectors.toList());

        // Find this method in the current scope
        Optional<MethodSymbol> method = scope.getMethodFinder().find(name, paramsTypes);
        if (method.isEmpty()){
            // The method hasn't been declared or parameters are wrong
            if (scope.getMethodFinder().find(name).isEmpty()){
                throw new SemanticException("El metodo no fue declarado", name);
            } else {
                throw new SemanticException("No se pudo encontrar un metodo adecuado para los parametros dados", name);
            }
        }
        methodSymbol = method.get();
    }

}
