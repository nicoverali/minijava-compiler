package semantic.symbol;

import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.List;

public interface MethodSymbol extends InnerLevelSymbol {
    /**
     * @return the {@link IsStaticAttribute} of this method which determines if the methodn is static or not
     */
    IsStaticAttribute isStatic();

    /**
     * @return the {@link Type} returned by this method
     */
    Type getReturnType();

    /**
     * @return the name of this method as a {@link String}
     */
    String getName();

    /**
     * @return the {@link NameAttribute} of this method which contains the name of it
     */
    NameAttribute getNameAttribute();

    /**
     * @return a list of all the {@link ParameterSymbol} of this method
     */
    List<ParameterSymbol> getParameters();

    /**
     * @return true if the given method has the same staticness, type, name, and parameters types, false otherwise
     */
    boolean equals(MethodSymbol method);
}
