package semantic.ast.scope;

import semantic.Variable;
import semantic.ast.block.LocalVariable;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.finder.MethodFinder;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

/**
 * Represents the current scope of a AST Node. This class knows the ClassSymbol container and the method or
 * constructor link to an AST Tree.
 */
public interface Scope {

    /**
     * Adds a new {@link LocalVariable} to this scope.
     *
     * @param variable a {@link LocalVariable}
     */
    void addLocalVariable(LocalVariable variable);

    /**
     * @return true if the current context is static, false if not
     */
    boolean isStaticContext();

    /**
     * Searches for a {@link Variable} with the given <code>name</code> in the current scope.
     * The order in which the variable will be search is:
     * <br>
     * <blockquote>
     * local variable -> parameters -> container attribute
     * </blockquote>
     *
     * @param name the name of the variable
     * @return an {@link Optional} wrapping a {@link Variable}
     */
    Optional<Variable> findVariable(NameAttribute name);

    /**
     * Returns a {@link MethodFinder} that can lookup method in this scope
     */
    MethodFinder getMethodFinder();

    /**
     * @return the {@link ClassSymbol} that contains this scope
     */
    ClassSymbol getClassContainer();

    /**
     * Returns the {@link Type} that's expected to be returned from this scope. It might be that no
     * return type is expected, in which case, the returned {@link Optional} will be empty
     *
     * @return an {@link Optional} wrapping the expected return {@link Type}
     */
    Optional<Type> getExpectedReturnType();

    /**
     * Creates and returns a sub-scope of this scope. A sub-scope will contain the same information about
     * the current context, but it will grow independently.
     * A sub-scope doesn't have any relationship with it super-scope other than containing the same context
     * just when it gets created.
     *
     * @return a new {@link Scope} that is sub-scope of this scope
     */
    Scope createSubScope();
}
