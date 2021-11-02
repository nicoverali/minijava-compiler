package semantic.ast.expression.access;

import semantic.ast.expression.access.chain.ChainNode;
import semantic.ast.scope.Scope;
import semantic.symbol.attribute.type.Type;

public abstract class BaseAccessNode implements AccessNode {

    private ChainNode chain;

    public BaseAccessNode() {
    }

    public BaseAccessNode(ChainNode chain) {
        this.chain = chain;
    }

    @Override
    public void setChain(ChainNode chain) {
        this.chain = chain;
    }

    @Override
    public AccessNode getChainEnd() {
        if (hasChainedAccess()){
            return chain.getChainEnd();
        }
        return this;
    }

    @Override
    public boolean hasChainedAccess() {
        return chain != null;
    }

    @Override
    public void validate(Scope scope) {
        validateAccess(scope);

        if (hasChainedAccess()){
            chain.validate(scope);
        }
    }

    /**
     * Validates this access. You shouldn't be worried about chained access within this method.
     *
     * @param scope the {@link Scope} where this access belongs
     */
    abstract protected void validateAccess(Scope scope);

    /**
     * Returns the type of this Access that's given as argument, or the type of the chain if it exists.
     *
     * @param thisType the type of this access
     * @return either the type of this access or the type of its chain if it has one
     */
    protected Type thisTypeOrChainType(Type thisType){
        return hasChainedAccess()
                ? chain.getType()
                : thisType;
    }

}
