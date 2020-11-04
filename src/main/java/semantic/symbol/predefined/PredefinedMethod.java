package semantic.symbol.predefined;

import lexical.Token;
import semantic.symbol.attribute.type.Type;

import java.util.Arrays;
import java.util.List;

/**
 * A predefined method is a member of a {@link PredefinedClass}.
 * It has a name, staticness, type and a list of {@link PredefinedParameter}
 */
public class PredefinedMethod {

    private final boolean isStatic;
    private final Type returnType;
    private final String name;
    private final List<PredefinedParameter> parameters;

    public static PredefinedMethod createStatic(Type type, String name, PredefinedParameter... parameters){
        return new PredefinedMethod(true, type, name, parameters);
    }

    public static PredefinedMethod createDynamic(Type type, String name, PredefinedParameter... parameters){
        return new PredefinedMethod(false, type, name, parameters);
    }

    private PredefinedMethod(boolean isStatic, Type returnType, String name, PredefinedParameter... parameters) {
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.name = name;
        this.parameters = Arrays.asList(parameters);
    }

    /**
     * @return true if the predefined method is static, false if not
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Returns the return {@link Type} of the method. Is type won't have a {@link Token} associated since it's
     * created at initialization and not read from a source code
     *
     * @return the return {@link Type} of the predefined method.
     */
    public Type getReturnType(){
        return returnType;
    }

    /**
     * @return the name of this predefined method as a {@link String}
     */
    public String getName() {
        return name;
    }
}
