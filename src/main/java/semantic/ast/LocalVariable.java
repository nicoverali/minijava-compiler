package semantic.ast;

import semantic.symbol.ClassSymbol;
import semantic.symbol.ParametrizedSymbol;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public class LocalVariable {

    private final Type type;
    private final NameAttribute name;

    private final ClassSymbol container;
    private final ParametrizedSymbol params;

    public LocalVariable(Type type, NameAttribute name, ClassSymbol container, ParametrizedSymbol params){
        this.type = type;
        this.name = name;
        this.container = container;
        this.params = params;
    }

}
