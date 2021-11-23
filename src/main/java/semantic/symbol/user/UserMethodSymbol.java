package semantic.symbol.user;

import semantic.SemanticException;
import semantic.ast.block.BlockNode;
import semantic.ast.scope.DynamicContextScope;
import semantic.ast.scope.StaticContextScope;
import semantic.ast.sentence.visitor.CodeFlowValidator;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static semantic.symbol.attribute.type.VoidType.VOID;

public class UserMethodSymbol implements MethodSymbol {

    private final IsStaticAttribute isStatic;
    private final Type returnType;
    private final NameAttribute name;
    private final BlockNode block;

    private final LinkedHashMap<String, ParameterSymbol> parameters = new LinkedHashMap<>(); // This type of map maintains order

    private ClassSymbol container;

    public UserMethodSymbol(IsStaticAttribute isStatic, Type returnType, NameAttribute name, List<ParameterSymbol> parameters, BlockNode block) {
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.name = name;
        parameters.forEach(this::add);
        this.block = block;
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

    @Override public IsStaticAttribute getStaticAttribute() {
        return isStatic;
    }

    @Override
    public boolean isStatic() {
        return isStatic.getValue();
    }

    @Override public Type getReturnType() {
        return returnType;
    }

    @Override
    public void validateBlock() {
        Type scopeReturnType = returnType.equals(VOID()) ? null : returnType;
        if (isStatic()){
            block.validate(new StaticContextScope(scopeReturnType, container, new ArrayList<>(parameters.values())));
        } else {
            block.validate(new DynamicContextScope(scopeReturnType, container, new ArrayList<>(parameters.values())));
        }

        if (returnType.equals(VOID())){
            new CodeFlowValidator().checkUnreachableCode(block.getSentences());
        } else if (!new CodeFlowValidator().doesReturnAlways(block.getSentences())){
            throw new SemanticException("Falta expresion return", name);
        }
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
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodSymbol)) return false;
        MethodSymbol that = (MethodSymbol) o;
        return Objects.equals(isStatic(), that.isStatic())
                && Objects.equals(getReturnType(), that.getReturnType())
                && Objects.equals(getName(), getName())
                && Objects.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        List<Object> hashes = new ArrayList<>(List.of(isStatic, returnType, name));
        hashes.addAll(parameters.values());
        return Objects.hash(hashes.toArray());
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        returnType.validate(SymbolTable.getInstance());
        parameters.values().forEach(param -> param.checkDeclaration(container));
    }

    @Override
    public void setContainer(ClassSymbol container) {
        this.container = container;
    }

    @Nullable
    @Override
    public ClassSymbol getContainer() {
        return container;
    }

    @Override
    public String toString() {
        String parametersStr = parameters.values().stream().map(ParameterSymbol::toString).collect(Collectors.joining(", "));
        return String.format("%s %s %s(%s)", isStatic, returnType, name, parametersStr);
    }
}
