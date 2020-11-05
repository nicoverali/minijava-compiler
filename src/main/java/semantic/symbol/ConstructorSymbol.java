package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.HashSet;
import java.util.List;

public class ConstructorSymbol implements InnerLevelSymbol {

    private final ReferenceType classReference;
    private final List<ParameterSymbol> parameters;

    private TopLevelSymbol topSymbol;

    public ConstructorSymbol(ReferenceType classReference, List<ParameterSymbol> parameters) {
        checkForDuplicates(parameters);
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

    private void checkForDuplicates(List<ParameterSymbol> parameters) throws SemanticException {
        HashSet<String> visited = new HashSet<>(parameters.size());
        NameAttribute duplicate = parameters.stream()
                .map(ParameterSymbol::getNameAttribute)
                .filter(name -> !visited.add(name.getValue()))
                .findFirst().orElse(null);

        if (duplicate != null){
            throw new SemanticException("Un constructor no puede tener dos argumentos con el mismo nombre", duplicate.getToken());
        }
    }

    @Override
    public void consolidate() throws SemanticException {
        parameters.forEach(ParameterSymbol::consolidate);
    }

    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        topSymbol = symbol;
        parameters.forEach(param -> param.setTopLevelSymbol(symbol));
    }
}
