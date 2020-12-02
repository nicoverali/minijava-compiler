package semantic.ast.access;

import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.access.chain.ChainNode;
import semantic.ast.expression.ExpressionNode;
import semantic.symbol.ClassSymbol;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConstructorAccessNode extends BaseAccessNode{

    private final SymbolTable ST = SymbolTable.getInstance();

    private final ReferenceType classRef;
    private final List<ExpressionNode> params;

    public ConstructorAccessNode(ReferenceType classRef, List<ExpressionNode> params) {
        this.classRef = classRef;
        this.params = params;
    }

    public ConstructorAccessNode(ChainNode chain, ReferenceType classRef, List<ExpressionNode> params) {
        super(chain);
        this.classRef = classRef;
        this.params = params;
    }

    @Override
    public boolean hasGenerics(TopLevelSymbol container) {
        Optional<Boolean> expGen = params.stream().map(expressionNode -> expressionNode.hasGenerics(container))
                                                    .filter(Boolean::booleanValue).findFirst();
        return super.hasGenerics(container) || classRef.isGeneric(container) || expGen.isPresent();
    }

    @Override
    public Type getType(Scope scope) {
        if (hasChainedAccess()){
            scope.setLeftChainType(classRef);
            return chain.getType(scope);
        }
        return classRef;
    }

    @Override
    public void validate(Scope scope) {
        classRef.validate(ST, scope.getClassContainer());
        Optional<ClassSymbol> symbol = ST.getClass(classRef);
        if (!symbol.isPresent()){
            throw new SemanticException("Solo se puede crear un objeto de tipo Clase", classRef);
        }
        Optional<ConstructorSymbol> constructor = symbol.get().getConstructor();
        if (constructor.isPresent()){
            ParamsChecker.validateParams(params, constructor.get().getParameters(), scope, classRef);
        } else if (params.size() > 0){
            throw new SemanticException("El constructor de la clase no tiene parametros", classRef);
        }

        if (hasChainedAccess()){
            scope.setLeftChainType(classRef);
            chain.validate(scope);
        }
    }

    @Override
    public NameAttribute getName() {
        return NameAttribute.of(classRef.getToken());
    }

    @Override
    public AccessNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics(container)){
            List<ExpressionNode> params = this.params.stream().map(exp -> exp.instantiate(container, newType)).collect(Collectors.toList());
            if (hasChainedAccess()){
                return new ConstructorAccessNode(chain.instantiate(container, newType), classRef.instantiate(container, newType), params);
            } else {
                return new ConstructorAccessNode(classRef.instantiate(container, newType), params);
            }
        }
        return this;
    }
}
