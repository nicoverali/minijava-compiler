package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConstructorSymbol implements InstantiableSymbol<ConstructorSymbol> {

    private final ReferenceType classReference;
    private final List<ParameterSymbol> parameters;

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
    public void checkDeclaration(ClassSymbol container) throws SemanticException, IllegalStateException {
        parameters.forEach(param -> param.checkDeclaration(container));

        if (!nameMatchesClassName(container))
            throw new SemanticException("El nombre del constructor no es el de la clase que lo contiene", getClassReference());
    }

    private boolean nameMatchesClassName(ClassSymbol container) {
        return container.getName().equals(this.classReference.getValue());
    }

    @Override
    public ConstructorSymbol instantiate(ClassSymbol container, String newType) {
        List<ParameterSymbol> params = parameters.stream().map(param -> param.instantiate(container, newType)).collect(Collectors.toList());
        return new ConstructorSymbol(classReference, params);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorSymbol that = (ConstructorSymbol) o;
        return Objects.equals(classReference, that.classReference) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classReference, parameters);
    }
}
