package semantic.symbol;

import semantic.ast.block.BlockNode;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.List;
import java.util.stream.Collectors;

public interface CallableSymbol extends InnerClassSymbol {

    /**
     * @return the {@link IsStaticAttribute} of this callable symbol which determines if it's static or not
     */
    IsStaticAttribute getStaticAttribute();

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

    /**
     * @return the {@link Type} returned by this callable symbol
     */
    Type getReturnType();

    /**
     * @return the {@link BlockNode} of this callable symbol
     */
    BlockNode getBlock();

}
