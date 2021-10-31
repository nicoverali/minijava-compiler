package semantic.ast.expression.access.chain;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.expression.access.AccessNode;
import semantic.ast.scope.Scope;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.ClassSymbol;
import semantic.symbol.finder.MethodFinder;
import semantic.symbol.MethodSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static semantic.symbol.attribute.IsStaticAttribute.emptyDynamic;

public class ChainedMethodNode extends BaseChainNode {

    private final SymbolTable ST = SymbolTable.getInstance();

    private final NameAttribute name;
    private final List<ExpressionNode> paramsExps;

    private MethodSymbol methodSymbol;

    public ChainedMethodNode(AccessNode leftAccess, Token dotToken, NameAttribute name, List<ExpressionNode> paramsExps) {
        super(leftAccess, dotToken);
        this.name = name;
        this.paramsExps = paramsExps;
    }

    public ChainedMethodNode(AccessNode leftAccess, Token dotToken, NameAttribute name, List<ExpressionNode> paramsExps, ChainNode chain) {
        super(leftAccess, dotToken, chain);
        this.name = name;
        this.paramsExps = paramsExps;
    }

    @Override
    public Type getType() {
        return thisTypeOrChainType(methodSymbol.getReturnType());
    }

    @Override
    public void validateAccess(Scope scope) {
        ClassSymbol classSym = validateLeftAccess();

        // Validate parameters and get the type of them
        paramsExps.forEach(exp -> exp.validate(scope));
        List<Type> paramsType = paramsExps.stream().map(ExpressionNode::getType).collect(Collectors.toList());

        // Find this method within the left referenced class
        Optional<MethodSymbol> method = new MethodFinder(classSym).find(emptyDynamic(), name, paramsType);
        if (method.isEmpty()){
            // Method is not declared or not suitable parameters found
            if (new MethodFinder(classSym).find(emptyDynamic(), name).isEmpty()){
                throw new SemanticException("Metodo no declarado", name);
            } else {
                throw new SemanticException("No se pudo encontrar un metodo adecuado para los parametros dados", name);
            }
        }
        methodSymbol = method.get();
    }

    @Override
    public NameAttribute getName() {
        return name;
    }

}
