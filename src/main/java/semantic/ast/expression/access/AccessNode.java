package semantic.ast.expression.access;

import lexical.Token;
import semantic.ast.expression.OperandNode;
import semantic.ast.expression.access.chain.ChainNode;
import semantic.symbol.attribute.NameAttribute;

public interface AccessNode extends OperandNode {

    /**
     * An access can consist of a single access and chained accesses after. This method will return the last
     * access node in the chain.
     * <br>
     * If this access doesn't have any chained access, the the last access will be this access itself.
     *
     * @return the last {@link AccessNode} in the chain of accesses of this node
     */
    AccessNode getChainEnd();

    /**
     * Checks whether this particular access has another access chained to the right.
     *
     * @return true if this access has a chained access, false otherwise
     */
    boolean hasChainedAccess();

    /**
     * Sets the next chain of this access
     *
     * @param chain the next access in the chain
     */
    void setChain(ChainNode chain);

}
