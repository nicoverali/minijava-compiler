package semantic.symbol;

import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.Collection;
import java.util.Optional;

public interface ClassSymbol extends TopLevelSymbol {
    /**
     * @return the {@link NameAttribute} of this class which contains the name of it
     */
    NameAttribute getNameAttribute();

    @Override
    String getName();

    /**
     * @return a collection of all the {@link AttributeSymbol} of this class
     */
    Collection<AttributeSymbol> getAttributes();

    /**
     * @return a collection of all the {@link MethodSymbol} of this class
     */
    Collection<MethodSymbol> getMethods();

    /**
     * @return an {@link Optional} wrapping a {@link ReferenceType} pointing to the class from which this class extends
     */
    Optional<ReferenceType> getParent();
}
