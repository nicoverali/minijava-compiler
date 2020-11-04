package semantic.symbol;

import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.List;

public class MethodSymbol {

    private final IsStaticAttribute isStatic;
    private final Type returnType;
    private final NameAttribute name;

    private final List<ParameterSymbol> parameters;

    public MethodSymbol(IsStaticAttribute isStatic, Type returnType, NameAttribute name, List<ParameterSymbol> parameters) {
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * Adds a list of {@link ParameterSymbol} to this method. The order in which parameters
     * are position in the list will be maintained
     * @param parameters a list of {@link ParameterSymbol} which will be added as parameters of this method
     */
    public void add(List<ParameterSymbol> parameters){
        parameters.forEach(this::add);
    }

    /**
     * Adds a {@link ParameterSymbol} to this method. The parameter will be positioned at the end of the method's
     * parameter list
     * @param parameter a {@link ParameterSymbol} which will be added as parameter of this method
     */
    public void add(ParameterSymbol parameter){
        parameters.add(parameter);
    }

    /**
     * @return the {@link IsStaticAttribute} of this method which determines if the methodn is static or not
     */
    public IsStaticAttribute isStatic() {
        return isStatic;
    }

    /**
     * @return the {@link Type} returned by this method
     */
    public Type getReturnType() {
        return returnType;
    }

    /**
     * @return the {@link NameAttribute} of this method which contains the name of it
     */
    public NameAttribute getName() {
        return name;
    }

    /**
     * @return a list of all the {@link ParameterSymbol} of this method
     */
    public List<ParameterSymbol> getParameters() {
        return parameters;
    }
}
