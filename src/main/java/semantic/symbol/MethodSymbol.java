package semantic.symbol;

import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.type.Type;

public interface MethodSymbol extends InnerClassSymbol, ParameterizedSymbol {
    /**
     * @return the {@link IsStaticAttribute} of this method which determines if the method is static or not
     */
    IsStaticAttribute getStaticAttribute();

    /**
     * @return true if the value of the {@link IsStaticAttribute} of this method is true, false otherwise.
     * @see #getStaticAttribute()
     */
    boolean isStatic();

    /**
     * @return the {@link Type} returned by this method
     */
    Type getReturnType();

    /**
     * Determines whether the given method is a valid overload of this method.
     * Methods with different name, although are not actually overloading, will always be considered valid.
     *
     * @param method another method
     * @return true if the given method is a valid overload of this method, false otherwise
     */
    default boolean isValidOverload(MethodSymbol method){
        return !getName().equals(method.getName())
                || !getParameters().equals(method.getParameters());
    }
}
