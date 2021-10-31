package semantic.ast;

import semantic.ast.scope.Scope;

public interface ASTNode {

    /**
     * Checks that this node has no semantic errors and also completes any left information about it.
     * <br>
     * Every implementation of {@link ASTNode} may have methods that need this specific method to run before
     * calling them.
     *
     * @param scope the current scope
     */
    void validate(Scope scope);

}
