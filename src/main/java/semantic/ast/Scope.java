package semantic.ast;

import semantic.Variable;
import semantic.symbol.MethodSymbol;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Optional;

public interface Scope {

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

    void setLeftChainType(Type leftChainType);

    TopLevelSymbol getTopContainer();
}
