package semantic.ast.expression.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.scope.Scope;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.ClassSymbol;
import semantic.symbol.finder.ConstructorFinder;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConstructorAccessNode extends BaseAccessNode{

    private final SymbolTable ST = SymbolTable.getInstance();

    private final ReferenceType classRef;
    private final List<ExpressionNode> paramsExpressions;

    public ConstructorAccessNode(ReferenceType classRef, List<ExpressionNode> paramsExpressions) {
        this.classRef = classRef;
        this.paramsExpressions = paramsExpressions;
    }

    public ConstructorAccessNode(ChainNode chain, ReferenceType classRef, List<ExpressionNode> paramsExpressions) {
        super(chain);
        this.classRef = classRef;
        this.paramsExpressions = paramsExpressions;
    }

    @Override
    public Type getType() {
        return thisTypeOrChainType(classRef);
    }

    @Override
    public void validateAccess(Scope scope) {
        // Check that class reference is valid and exists
        classRef.validate(ST);
        Optional<ClassSymbol> clazz = ST.getClass(classRef);
        if (clazz.isEmpty()){
            throw new SemanticException("No se pudo encontrar la clase", classRef);
        }

        // Validate expressions and get types of them
        paramsExpressions.forEach(exp -> exp.validate(scope));
        List<Type> params = paramsExpressions.stream().map(exp -> getType()).collect(Collectors.toList());

        // Find constructor with the given parameters types
        Optional<ConstructorSymbol> constructor = new ConstructorFinder(clazz.get()).find(params);
        if (constructor.isEmpty()) throw new SemanticException("No se pudo encontrar un constructor con estos parametros", classRef);

    }

    @Override
    public Token toToken() {
        return classRef.getToken();
    }

}
