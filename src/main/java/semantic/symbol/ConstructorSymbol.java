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

    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        topSymbol = symbol;
        parameters.forEach(param -> param.setTopLevelSymbol(symbol));
    }

    private void checkForDuplicates(List<ParameterSymbol> parameters) throws SemanticException {
        HashSet<String> visited = new HashSet<>(parameters.size());
        NameAttribute duplicate = parameters.stream()
                .map(ParameterSymbol::getNameAttribute)
                .filter(name -> !visited.add(name.getValue()))
                .findFirst().orElse(null);

        if (duplicate != null){
            throw new SemanticException("Un constructor no puede tener dos argumentos con el mismo nombre", duplicate);
        }
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public NameAttribute getNameAttribute() {
        return NameAttribute.predefined("");
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        parameters.forEach(ParameterSymbol::checkDeclaration);

        if (topSymbol == null)
            throw new IllegalStateException("El constructor no esta contenido dentro de ninguna clase");
        if (!constructorNameMatchesClass())
            throw new SemanticException("El nombre del constructor no es el de la clase que lo contiene", getClassReference());
    }

    private boolean constructorNameMatchesClass() {
        return topSymbol.getName().equals(this.classReference.getValue());
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {

    }
}
