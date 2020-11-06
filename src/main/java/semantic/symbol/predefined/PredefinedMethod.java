package semantic.symbol.predefined;

import semantic.SemanticException;
import semantic.symbol.MethodSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A predefined method is a member of a {@link PredefinedClass}.
 * It has a name, staticness, type and a list of {@link PredefinedParameter}
 */
public class PredefinedMethod implements MethodSymbol {

    private final IsStaticAttribute isStatic;
    private final Type returnType;
    private final NameAttribute name;
    private final List<PredefinedParameter> parameters;

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
    public IsStaticAttribute isStatic() {
        return isStatic;
    }

    @Override
    public Type getReturnType(){
        return returnType;
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
        if (parameters.size() != method.getParameters().size()) return false;
        for (int i = 0; i < parameters.size(); i++) {
            if (!parameters.get(i).equals(method.getParameters().get(i))){
                return false;
            }
        }
        return true;
    }


    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        // Unnecessary
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {
        // Do nothing
    }
}
