package semantic.ast.expression.access;

import semantic.SemanticException;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.finder.MethodFinder;
import semantic.symbol.MethodSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static semantic.symbol.attribute.IsStaticAttribute.emptyStatic;

public class StaticMethodAccessNode extends BaseAccessNode {

    private final SymbolTable ST = SymbolTable.getInstance();

    private final ReferenceType classRef;
    private final NameAttribute name;
    private final List<ExpressionNode> paramsExps;

    private MethodSymbol methodSymbol;

    public StaticMethodAccessNode(ReferenceType classRef, NameAttribute name, List<ExpressionNode> paramsExps) {
        this.classRef = classRef;
        this.name = name;
        this.paramsExps = paramsExps;
    }

    public StaticMethodAccessNode(ChainNode chain, ReferenceType classRef, NameAttribute name, List<ExpressionNode> paramsExps) {
        super(chain);
        this.classRef = classRef;
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
    public void validateAccess(Scope scope) {
        // Validate class reference
        classRef.validate(ST);

        // Validate param expressions and get the type of them
        paramsExps.forEach(exp -> exp.validate(scope));
        List<Type> paramsTypes = paramsExps.stream().map(ExpressionNode::getType).collect(Collectors.toList());

        // Find the method inside the referenced class
        Optional<MethodSymbol> method = ST.getClass(classRef)
                .flatMap(clazz -> new MethodFinder(clazz).find(emptyStatic(), name, paramsTypes));
        if (method.isEmpty()){
            throw new SemanticException("No se pudo encontrar el metodo", name);
        }
        methodSymbol = method.get();
    }

    @Override
    public NameAttribute getName() {
        return name;
    }
}