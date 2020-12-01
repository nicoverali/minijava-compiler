package semantic.ast.access;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.Scope;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

public abstract class BaseChainNode extends BaseAccessNode implements ChainNode{

    protected final Token dotToken;

    public BaseChainNode(Token dotToken) {
        this.dotToken = dotToken;
    }

    public BaseChainNode(ChainNode chain, Token dotToken) {
        super(chain);
        this.dotToken = dotToken;
    }

    @Override
    public Type getType(Scope scope) {
        Type thisType = getTypeFrom((ReferenceType) scope.getLeftChainType(), scope);
        if(hasChainedAccess()){
            scope.setLeftChainType(thisType);
            return chain.getType(scope);
        }
        return thisType;

    }

    @Override
    public void validate(Scope scope) {
        Type leftType = scope.getLeftChainType();
        if (leftType instanceof ReferenceType){
            validate((ReferenceType) leftType, scope);
            if (hasChainedAccess()){
                Type thisType = getTypeFrom((ReferenceType) leftType, scope);
                scope.setLeftChainType(thisType);
                chain.validate(scope);
            }
        } else {
            throw new SemanticException("Solo se puede encadenar un tipo referencia", dotToken);
        }
    }

    /**
     * Validates this chain node assuming that the left type is a reference type
     *
     * @param leftType the left access type
     * @param scope the current scope
     */
    protected abstract void validate(ReferenceType leftType, Scope scope);

    /**
     * Takes the type of the left access, and returns this chain type based on it.
     *
     * @param leftChainType the left access
     * @return the {@link Type} of this chained access
     */
    abstract protected Type getTypeFrom(ReferenceType leftChainType, Scope scope);
}
