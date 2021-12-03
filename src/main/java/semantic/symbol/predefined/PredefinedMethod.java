package semantic.symbol.predefined;

import semantic.SemanticException;
import semantic.ast.block.BlockNode;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A predefined method is a member of a {@link PredefinedClass}.
 * It has a name, staticness, type and a list of {@link PredefinedParameter}
 */
public class PredefinedMethod implements MethodSymbol {

    private final IsStaticAttribute isStatic;
    private final Type returnType;
    private final NameAttribute name;
    private final List<PredefinedParameter> parameters;

    private ClassSymbol container;

    public static PredefinedMethod createStatic(Type type, String name, PredefinedParameter... parameters){
        return new PredefinedMethod(IsStaticAttribute.emptyStatic(), type, name, parameters);
    }

    public static PredefinedMethod createDynamic(Type type, String name, PredefinedParameter... parameters){
        return new PredefinedMethod(IsStaticAttribute.emptyDynamic(), type, name, parameters);
    }

    private PredefinedMethod(IsStaticAttribute isStatic, Type returnType, String name, PredefinedParameter... parameters) {
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.name = NameAttribute.predefined(name);
        this.parameters = Arrays.asList(parameters);
    }


    @Override
    public IsStaticAttribute getStaticAttribute() {
        return isStatic;
    }

    @Override
    public boolean isStatic() {
        return isStatic.getValue();
    }

    @Override
    public Type getReturnType(){
        return returnType;
    }

    @Override
    public void validateBlock() {
        // Predefined methods don't have blocks
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public NameAttribute getNameAttribute() {
        return name;
    }

    @Override
    public List<ParameterSymbol> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    @Override
    public BlockNode getBlock() {
        return BlockNode.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodSymbol)) return false;
        MethodSymbol that = (MethodSymbol) o;
        return Objects.equals(isStatic(), that.isStatic())
                && Objects.equals(getReturnType(), that.getReturnType())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isStatic, returnType, name, parameters);
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        // Do nothing
    }

    @Override
    public void setContainer(ClassSymbol container) {
        this.container = container;
    }

    @Override
    public ClassSymbol getContainer() {
        return container;
    }

    @Override
    public String toString() {
        String parametersStr = parameters.stream().map(ParameterSymbol::toString).collect(Collectors.joining(", "));
        return String.format("Predefined -> %s %s %s(%s)", isStatic, returnType, name, parametersStr);
    }
}
