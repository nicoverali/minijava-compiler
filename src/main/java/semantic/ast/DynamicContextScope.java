package semantic.ast;

import semantic.Variable;
import semantic.symbol.ClassSymbol;
import semantic.symbol.MethodSymbol;
import semantic.symbol.ParameterSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DynamicContextScope implements Scope {

    private final ClassSymbol container;
    private final List<ParameterSymbol> parameters;
    private final List<LocalVariable> localVariables = new ArrayList<>();

    private Type leftChainType; // This is use by Access classes

    public DynamicContextScope(ClassSymbol container, List<ParameterSymbol> parameters) {
        this.container = container;
        this.parameters = parameters;
    }

    @Override
    public void addLocalVariable(LocalVariable variable) {
        localVariables.add(variable);
    }

    @Override
    public boolean isStaticContext() {
        return false;
    }

    @Override
    public Optional<Variable> findVariable(NameAttribute name){
        return this.findVariable(name.getValue());
    }

    @Override
    public Optional<Variable> findVariable(String name){
        Variable var = searchIn(localVariables, name);
        if (var != null) return Optional.of(var);
        var = searchIn(parameters, name);
        if (var != null) return Optional.of(var);

        return container.getAttributes().find(name).map(attr -> attr);
    }

    private Variable searchIn(List<? extends Variable> list, String name) {
        for (Variable var : list){
            if (var.getNameAttribute().getValue().equals(name)){
                return var;
            }
        }
        return null;
    }

    @Override
    public Optional<MethodSymbol> findMethod(String name){
        return container.getMethods().find(name);
    }

    @Override
    public Optional<MethodSymbol> findMethod(NameAttribute name){
        return container.getMethods().find(name);
    }

    @Override
    public Type getLeftChainType() {
        return leftChainType;
    }

    @Override
    public void setLeftChainType(Type leftChainType) {
        this.leftChainType = leftChainType;
    }

    @Override
    public ClassSymbol getClassContainer(){
        return container;
    }

}
