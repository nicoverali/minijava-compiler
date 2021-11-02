package semantic.symbol.finder;

import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.ConstructorSymbol;
import semantic.symbol.attribute.type.Type;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This is a helper class to retrieve {@link ConstructorSymbol} according to different conditions.
 */
public class ConstructorFinder {

    private final Collection<ConstructorSymbol> constructors;

    public ConstructorFinder(Collection<ConstructorSymbol> constructors) {
        this.constructors = constructors;
    }

    public ConstructorFinder(ClassSymbol container) {
        this.constructors = container.getConstructors();
    }

    /**
     * Searches for a {@link ConstructorSymbol} that match the given parameters types.
     *
     * @param parameters a list of {@link Type} of the parameters of a constructor
     * @return an {@link Optional} wrapping a {@link ConstructorSymbol} that matches the list of parameters given
     */
    public Optional<ConstructorSymbol> find(List<Type> parameters){
        List<ConstructorSymbol> result = constructors.stream()
                .filter(cons -> ParametersValidation.conforms(parameters, cons.getParametersTypes()))
                .collect(Collectors.toList());

        if (result.size() > 1) throw new SemanticException("Hay mas de un constructor con mismos parametros", result.get(1));
        return result.stream().findFirst();
    }

}
