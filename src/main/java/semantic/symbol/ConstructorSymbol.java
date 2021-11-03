package semantic.symbol;

import semantic.SemanticException;
import semantic.ast.block.BlockNode;
import semantic.ast.scope.DynamicContextScope;
import semantic.ast.sentence.visitor.CodeFlowValidator;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ConstructorSymbol implements InnerClassSymbol, ParameterizedSymbol {

    private final ReferenceType classReference;
    private final List<ParameterSymbol> parameters;
    private final BlockNode block;

    private ClassSymbol container;

    public ConstructorSymbol(ReferenceType classReference, List<ParameterSymbol> parameters, BlockNode block) {
        checkForDuplicates(parameters);
        this.classReference = classReference;
        this.parameters = parameters;
        this.block = block;
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
    public boolean hasParameters() {
        return !parameters.isEmpty();
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
        this.container = container;
        parameters.forEach(param -> param.checkDeclaration(container));
        if (!nameMatchesClassName(container))
            throw new SemanticException("El nombre del constructor no es el de la clase que lo contiene", getClassReference());
    }

    /**
     * Validates the block of this method
     */
    public void validateBlock() {
        block.validate(new DynamicContextScope(container, parameters));
        new CodeFlowValidator().checkUnreachableCode(block.getSentences());
    }

    private boolean nameMatchesClassName(ClassSymbol container) {
        return container.getName().equals(this.classReference.getValue());
    }

    /**
     * Determines whether the given constructor is a valid overload of this constructor.
     * Constructors of different classes, although are not actually overloading, will always be considered valid.
     *
     * @param constructor another constructor
     * @return true if the given constructor is a valid overload of this constructor, false otherwise
     */
    boolean isValidOverload(ConstructorSymbol constructor){
        return !classReference.equals(constructor.getClassReference())
                || !parameters.equals(constructor.parameters);
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
