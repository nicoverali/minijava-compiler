package semantic.ast.asm;

import asm.offset.ASMOffsetsGenerator;
import semantic.ast.block.LocalVariable;
import semantic.symbol.*;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.ArrayList;
import java.util.List;

public class ASMContext {

    private final CallableSymbol container;
    private final ASMOffsetsGenerator offsets;
    private final IsStaticAttribute isStatic;
    private final String mallocLabel;

    private final List<ParameterSymbol> parameters;
    private final List<LocalVariable> variables = new ArrayList<>();

    public ASMContext(CallableSymbol container, ASMOffsetsGenerator offsets, String mallocLabel) {
        this.container = container;
        this.isStatic = container.getStaticAttribute();
        this.offsets = offsets;
        this.mallocLabel = mallocLabel;

        parameters = container.getParameters();
    }

    public void addVariable(LocalVariable variable) {
        variables.add(variable);
    }

    public int numberOfVariables(){
        return variables.size();
    }

    public ASMContext createSubContext(){
        ASMContext subContext = new ASMContext(container, offsets, mallocLabel);
        variables.forEach(subContext::addVariable);
        return subContext;
    }

    public List<ParameterSymbol> getParameters(){
        return container.getParameters();
    }

    public Type getReturnType() {
        return container.getReturnType();
    }

    public boolean isStatic() {
        return isStatic.getValue();
    }

    public boolean isDynamic() {
        return !isStatic.getValue();
    }

    public int getOffsetOf(AttributeSymbol attr) {
        return offsets.getAttributeOffset(attr);
    }

    public int getOffsetOf(MethodSymbol method) {
        return offsets.getMethodOffset(method);
    }

    public int getOffsetOf(ParameterSymbol parameter) {
        int paramIdx = parameters.indexOf(parameter);
        int paramOffset = parameters.size() - paramIdx + 2;
        if (isDynamic()) paramOffset++;
        return paramOffset;
    }

    public int getOffsetOf(LocalVariable variable) {
        return (-variables.indexOf(variable));
    }

    public String getMallocLabel(){
        return mallocLabel;
    }
}
