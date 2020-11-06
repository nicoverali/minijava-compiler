package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.MethodSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.*;

public class UserMethodSymbol implements MethodSymbol {

    private final IsStaticAttribute isStatic;
    private final Type returnType;
    private final NameAttribute name;

    private TopLevelSymbol topSymbol;

    private final LinkedHashMap<String, ParameterSymbol> parameters = new LinkedHashMap<>(); // This type of map maintains order

    public UserMethodSymbol(IsStaticAttribute isStatic, Type returnType, NameAttribute name, List<ParameterSymbol> parameters) {
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.name = name;
        parameters.forEach(this::add);
    }

    /**
     * Adds a list of {@link ParameterSymbol} to this method. The order in which parameters
     * are position in the list will be maintained.
     * <br>
     * If the method already had a parameter with the same name as one of the new ones, then an exception will be thrown
     *
     * @param parameters a list of {@link ParameterSymbol} which will be added as parameters of this method
     * @throws SemanticException if the method already had a parameter with the same name as one of the new ones
     */
    public void add(List<ParameterSymbol> parameters) throws SemanticException{
        parameters.forEach(this::add);
    }

    /**
     * Adds a {@link ParameterSymbol} to this method. The parameter will be positioned at the end of the method's
     * parameter list.
     * <br>
     * If the method already had a parameter with the same name as one of the new ones, then an exception will be thrown
     *
     * @param parameter a {@link ParameterSymbol} which will be added as parameter of this method
     * @throws SemanticException if the method already had a parameter with the same name as the new one
     */
    public void add(ParameterSymbol parameter) throws SemanticException {
        NameAttribute paramName = parameter.getNameAttribute();
        if (parameters.containsKey(paramName.getValue())){
            throw new SemanticException("Un metodo no puede tener dos parametros con el mismo nombre", paramName.getToken());
        }
        parameter.setTopLevelSymbol(topSymbol);
        parameters.put(paramName.getValue(), parameter);
    }

    @Override public IsStaticAttribute isStatic() {
        return isStatic;
    }

    @Override public Type getReturnType() {
        return returnType;
    }

    @Override public String getName(){
        return name.getValue();
    }

    @Override public NameAttribute getNameAttribute() {
        return name;
    }

    @Override public List<ParameterSymbol> getParameters() {
        return new ArrayList<>(parameters.values());
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {
        if (topSymbol == null) throw new IllegalStateException("El metodo no esta contenido dentro de ningun simbolo de nivel superior");
        returnType.validate(SymbolTable.getInstance(), topSymbol);
        parameters.values().forEach(ParameterSymbol::consolidate);
    }

    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        topSymbol = symbol;
        parameters.values().forEach(param -> param.setTopLevelSymbol(symbol));
    }
}
