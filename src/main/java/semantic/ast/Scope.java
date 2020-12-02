package semantic.ast;

import semantic.Variable;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

/**
 * Represents the current scope of a AST Node. This class knows the ClassSymbol container and the method or
 * constructor link to an AST Tree.
 * <br>
 * Note that an {@link semantic.symbol.InterfaceSymbol} cannot have blocks, thus doesn't make sense for a Scope to
 * have an interface as a container.
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
    Optional<Variable> findVariable(String name);

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
     * Searches for a {@link MethodSymbol} with the given <code>name</code> in the current scope.
     * This method must be a memeber of the current {@link TopLevelSymbol} container.
     *
     * @param name the name of the method
     * @return an {@link Optional} wrapping a {@link MethodSymbol}
     */
    Optional<MethodSymbol> findMethod(String name);

    /**
     * Searches for a {@link MethodSymbol} with the given <code>name</code> in the current scope.
     * This method must be a memeber of the current {@link TopLevelSymbol} container.
     *
     * @param name the name of the method
     * @return an {@link Optional} wrapping a {@link MethodSymbol}
     */
    Optional<MethodSymbol> findMethod(NameAttribute name);

    /**
     * @return the last set
     */
    Type getLeftChainType();

    /**
     * Sets the {@link Type} of the last left {@link semantic.ast.access.AccessNode}
     *
     * @param leftChainType left type
     */
    void setLeftChainType(Type leftChainType);

    /**
     * @return the {@link ClassSymbol} that contains this scope
     */
    ClassSymbol getClassContainer();
}
