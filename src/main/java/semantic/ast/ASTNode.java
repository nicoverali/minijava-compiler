package semantic.ast;

import semantic.symbol.TopLevelSymbol;

public interface ASTNode {

    /**
     * Checks that this node has no semantic errors and also completes any left information about it.
     * <br>
     * Every implementation of {@link ASTNode} may have methods that need this specific method to run before
     * calling them.
     *
     * @param scope a list of {@link LocalVariable} that conform the local scope of this sentence
     */
    void validate(Scope scope);

    /**
     * Instantiates every generic type of this node and returns the new instance.
     * <br>
     * If the node doesn't have a generic type, it may return itself, since nodes are immutables
     * <br><br>
     * This method should always be called after {@link #validate(Scope)}, otherwise the node won't
     * be able to detect its generic types
     *
     * @see #validate(Scope)
     * @param container the {@link TopLevelSymbol} that contains this AST
     * @param newType the name of the type which any generic type will be instantiated to
     * @return an instance of this node with all generic types instantiated
     */
     ASTNode instantiate(TopLevelSymbol container, String newType);

    /**
     * Checks whether this node has a generic type in it or not. Note that the node itself may not contain
     * any generic type, but it may have child that does have one.
     * <br><br>
     * This method should always be called after {@link #validate(Scope)}, otherwise the node won't
     * be able to detect its generic types
     *
     * @see #validate(Scope)
     * @return true if this node or any of its child have at least one generic type, false otherwise
     */
    boolean hasGenerics();

}
