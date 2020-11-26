package semantic.ast.access;

import semantic.ast.expression.OperandNode;

public interface AccessNode extends OperandNode {

    /**
     * An access can consist of a single access and chained accesses after. This method will return the last
     * access node in the chain.
     * <br>
     * If this access doesn't have any chained access, the the last access will be this access itself.
     *
     * @return the last {@link AccessNode} in the chain of accesses of this node
     */
    AccessNode getLastAccess();

}
