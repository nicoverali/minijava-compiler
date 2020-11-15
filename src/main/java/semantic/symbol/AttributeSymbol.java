package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.IsPublicAttribute;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

public class AttributeSymbol implements InnerLevelSymbol {

    private IsPublicAttribute isPublic = IsPublicAttribute.defaultAttribute();
    private IsStaticAttribute isStatic = IsStaticAttribute.defaultAttribute();
    private final Type type;
    private final NameAttribute name;

    private TopLevelSymbol topSymbol;

    public AttributeSymbol(Type type, NameAttribute name) {
        this.type = type;
        this.name = name;
    }

    public AttributeSymbol(IsStaticAttribute isStatic, Type type, NameAttribute name) {
        this.isStatic = isStatic;
        this.type = type;
        this.name = name;
    }

    public AttributeSymbol(IsPublicAttribute isPublic, IsStaticAttribute isStatic, Type type, NameAttribute name) {
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.type = type;
        this.name = name;
    }

    /**
     * @return the {@link IsPublicAttribute} of this attribute, which determines if the attribute is public or not
     */
    public IsPublicAttribute isPublic() {
        return isPublic;
    }

    /**
     * @return the {@link IsStaticAttribute} of this attribute, which determines if the attribute is static or not
     */
    public IsStaticAttribute isStatic() {
        return isStatic;
    }

    /**
     * @return the {@link Type} of this attribute
     */
    public Type getType(){
        return type;
    }

    @Override
    public NameAttribute getNameAttribute() {
        return name;
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        if (topSymbol == null) throw new IllegalStateException("El atributo no esta contenido dentro de ninguna clase");
        type.validate(SymbolTable.getInstance(), topSymbol);
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {

    }

    @Override
    public void setTopLevelSymbol(TopLevelSymbol symbol) {
        topSymbol = symbol;
    }
}
