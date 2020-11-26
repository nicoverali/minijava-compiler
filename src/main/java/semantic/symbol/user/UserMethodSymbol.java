package semantic.symbol.user;

import semantic.SemanticException;
import semantic.symbol.MethodSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.attribute.type.Type;

import java.util.*;
import java.util.stream.Collectors;

public class UserMethodSymbol implements MethodSymbol {

    private final IsStaticAttribute isStatic;
    private final Type returnType;
    private final NameAttribute name;

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
            throw new SemanticException("Un metodo no puede tener dos parametros con el mismo nombre", paramName);
        }
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
    public boolean equals(Object obj) {
        return obj instanceof MethodSymbol && equals((MethodSymbol) obj);
    }

    @Override
    public boolean equals(MethodSymbol method) {
        return this.isStatic.equals(method.isStatic())
                && this.returnType.equals(method.getReturnType())
                && this.name.equals(method.getNameAttribute())
                && parametersAreEqual(method);
    }

    private boolean parametersAreEqual(MethodSymbol method) {
        List<ParameterSymbol> ours = new ArrayList<>(parameters.values());
        List<ParameterSymbol> theirs = method.getParameters();
        if (ours.size() != theirs.size()) return false;
        for (int i = 0; i < ours.size(); i++) {
            if (!ours.get(i).equals(theirs.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public void checkDeclaration(TopLevelSymbol container) throws SemanticException, IllegalStateException {
        returnType.validate(SymbolTable.getInstance(), container);
        parameters.values().forEach(param -> param.checkDeclaration(container));
    }

    @Override
    public void consolidate(TopLevelSymbol container) throws SemanticException, IllegalStateException {

    }

    @Override
    public MethodSymbol instantiate(TopLevelSymbol container, String newType) {
        List<ParameterSymbol> params = parameters.values().stream()
                .map(param -> param.instantiate(container, newType)).collect(Collectors.toList());
        if (returnType instanceof ReferenceType){
            return new UserMethodSymbol(isStatic, ((ReferenceType) returnType).instantiate(container, newType), name, params);
        }
        return new UserMethodSymbol(isStatic, returnType, name, params);
    }
}
