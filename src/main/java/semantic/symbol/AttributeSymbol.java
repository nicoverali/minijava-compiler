package semantic.symbol;

import semantic.SemanticException;
import semantic.Variable;
import semantic.symbol.attribute.IsPublicAttribute;
import semantic.symbol.attribute.IsStaticAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.Type;

import javax.annotation.Nullable;

public class AttributeSymbol implements InnerClassSymbol, Variable {

    private IsPublicAttribute isPublic = IsPublicAttribute.defaultAttribute();
    private IsStaticAttribute isStatic = IsStaticAttribute.defaultAttribute();
    private final Type type;
    private final NameAttribute name;

    private ClassSymbol container;

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
    public IsPublicAttribute getPublicAttribute() {
        return isPublic;
    }

    /**
     * @return the {@link IsStaticAttribute} of this attribute, which determines if the attribute is static or not
     */
    public IsStaticAttribute getStaticAttribute() {
        return isStatic;
    }

    /**
     * @return true if the value of the {@link IsPublicAttribute} of this attribute is true, false otherwise
     * @see #getPublicAttribute()
     */
    public boolean isPublic(){
        return isPublic.getValue();
    }

    /**
     * @return true if the value of the {@link IsPublicAttribute} of this attribute is false, false otherwise
     * @see #getPublicAttribute()
     */
    public boolean isPrivate(){
        return !isPublic.getValue();
    }

    /**
     * @return true if the value of the {@link IsStaticAttribute} of this attribute is true, false otherwise
     * @see #getStaticAttribute()
     */
    public boolean isStatic(){
        return isStatic.getValue();
    }

    /**
     * @return true if the value of the {@link IsPublicAttribute} of this attribute is false, false otherwise
     * @see #getStaticAttribute()
     */
    public boolean isDynamic(){
        return !isStatic.getValue();
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
        type.validate(SymbolTable.getInstance());
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
}
