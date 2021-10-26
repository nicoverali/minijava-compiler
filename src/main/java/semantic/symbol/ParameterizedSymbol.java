package semantic.symbol;

import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.stream.Collectors;

public interface ParameterizedSymbol extends Symbol {

    /**
     * @return a list of all the {@link ParameterSymbol} of this method
     */
    List<ParameterSymbol> getParameters();

    /**
     * @return true if the method has at least one parameter, false if not
     */
    boolean hasParameters();

    /**
     * @return an ordered list of the {@link Type} of each {@link ParameterSymbol} of this method
     */
    default List<Type> getParametersTypes(){
        return getParameters().stream()
                .map(ParameterSymbol::getType)
                .collect(Collectors.toList());
    }

}
