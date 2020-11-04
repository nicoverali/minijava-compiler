package syntactic.entity;

import syntactic.entity.attribute.type.ReferenceType;

import java.util.List;

public class ConstructorSymbol {

    private final ReferenceType classReference;
    private final List<ParameterSymbol> parameters;

    public ConstructorSymbol(ReferenceType classReference, List<ParameterSymbol> parameters) {
        this.classReference = classReference;
        this.parameters = parameters;
    }

    /**
     * @return a {@link ReferenceType} pointing to the class to which this constructor belongs
     */
    public ReferenceType getClassReference() {
        return classReference;
    }

    /**
     * @return a list of all the {@link ParameterSymbol} of this constructor
     */
    public List<ParameterSymbol> getParameters() {
        return parameters;
    }
}
