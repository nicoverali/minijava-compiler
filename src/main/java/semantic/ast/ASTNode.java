package semantic.ast;

import java.util.List;

public interface ASTNode {

    /**
     * Checks that this node has no semantic errors and also completes any left information about it.
     * <br>
     * Every implementation of {@link ASTNode} may have methods that need this specific method to run before
     * calling them.
     *
     * @param scope a list of {@link LocalVariable} that conform the local scope of this sentence
     */
    void validate(LocalScope scope);

}
