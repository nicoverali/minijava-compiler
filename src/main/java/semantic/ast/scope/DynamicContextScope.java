package semantic.ast.scope;

import semantic.Variable;
import semantic.ast.block.LocalVariable;
import semantic.symbol.*;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;
import semantic.symbol.finder.AttributeFinder;
import semantic.symbol.finder.MethodFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static semantic.symbol.attribute.IsStaticAttribute.emptyDynamic;


public class DynamicContextScope implements Scope {

    private final Type returnType;
    private final ClassSymbol container;
    private final List<ParameterSymbol> parameters;
    private final List<LocalVariable> localVariables = new ArrayList<>();


    public DynamicContextScope(Type returnType, ClassSymbol container, List<ParameterSymbol> parameters) {
        this.returnType = returnType;
        this.container = container;
        this.parameters = parameters;
    }

    public DynamicContextScope(ClassSymbol container, List<ParameterSymbol> parameters) {
        this.returnType = null;
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
        Variable var = searchIn(localVariables, name);
        if (var != null) return Optional.of(var);
        var = searchIn(parameters, name);
        if (var != null) return Optional.of(var);

        return new AttributeFinder(container).find(emptyDynamic(), name).map(attr -> attr);
    }

    private Variable searchIn(List<? extends Variable> list, NameAttribute name) {
        for (Variable var : list){
            if (var.getNameAttribute().equals(name)){
                return var;
            }
        }
        return null;
    }

    @Override
    public MethodFinder getMethodFinder(){
        return new MethodFinder(container);
    }

    @Override
    public ClassSymbol getClassContainer(){
        return container;
    }

    @Override
    public Optional<Type> getExpectedReturnType() {
        return Optional.ofNullable(returnType);
    }

    @Override
    public Scope createSubScope() {
        Scope subScope = new DynamicContextScope(returnType, container, parameters);
        localVariables.forEach(subScope::addLocalVariable);
        return subScope;
    }


}
