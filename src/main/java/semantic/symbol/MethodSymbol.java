package semantic.symbol;

import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.List;

public interface MethodSymbol extends InstantiableSymbol<MethodSymbol> {
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
     * @return a list of all the {@link ParameterSymbol} of this method
     */
    List<ParameterSymbol> getParameters();

    /**
     * @return true if the method has at least one parameter, false if not
     */
    boolean hasParameters();
}
