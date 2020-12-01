package semantic.ast.access;

import semantic.ast.access.chain.ChainNode;
import semantic.symbol.TopLevelSymbol;

public abstract class BaseAccessNode implements AccessNode {

    protected ChainNode chain;

    public BaseAccessNode() {
    }

    public BaseAccessNode(ChainNode chain) {
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
    public boolean hasGenerics(TopLevelSymbol container) {
        return hasChainedAccess() && chain.hasGenerics(container);
    }
}
