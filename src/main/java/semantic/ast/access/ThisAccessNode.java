package semantic.ast.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.ast.access.chain.ChainNode;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

public class ThisAccessNode extends BaseAccessNode {

    private Token thisToken;

    public ThisAccessNode(Token thisToken) {
        this.thisToken = thisToken;
    }

    public ThisAccessNode(ChainNode chain, Token thisToken) {
        super(chain);
        this.thisToken = thisToken;
    }

    @Override
    public Type getType(Scope scope) {
        Type thisType = new ReferenceType(scope.getClassContainer().getName());
        if (hasChainedAccess()){
            scope.setLeftChainType(thisType);
            return chain.getType(scope);
        }
        return thisType;
    }

    @Override
    public void validate(Scope scope) {
        if (scope.isStaticContext()){
            throw new SemanticException("No se puede acceder a this en un contexto estatico", thisToken);
        }
    }

    @Override
    public NameAttribute getName() {
        return NameAttribute.of(thisToken);
    }

    @Override
    public AccessNode instantiate(TopLevelSymbol container, String newType) {
        if (hasGenerics(container)){
            return new ThisAccessNode(chain.instantiate(container, newType), thisToken);
        }
        return this;
    }
}
