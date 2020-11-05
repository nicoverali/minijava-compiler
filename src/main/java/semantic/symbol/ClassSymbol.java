package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;

public class ClassSymbol implements TopLevelSymbol {

    private final NameAttribute name;
    private GenericityAttribute generic;

    private ConstructorSymbol constructor;
    private final Map<String, AttributeSymbol> attributes = new HashMap<>();
    private final Map<String, MethodSymbol> methods = new HashMap<>();

    private ReferenceType parent;
    private final List<ReferenceType> interfaces = new ArrayList<>();

    public ClassSymbol(NameAttribute name){
        this.name = name;
    }

    /**
     * Adds a {@link ReferenceType} pointing to a class from which this class extends.
     * If this class already extended another class, then an exception will be thrown
     *
     * @param classReference a {@link ReferenceType} pointing to a class extended by this class
     * @throws SemanticException if the class was already extending another class
     */
    public void addExtends(ReferenceType classReference) throws SemanticException {
        if (parent != null)
            throw new SemanticException("Las clases solo pueden extender una unica clase", classReference.getToken());
        parent = classReference;
    }

    /**
     * Adds a {@link ReferenceType} pointing to an interface which this class implements
     *
     * @param interfaceReference a {@link ReferenceType} pointing to an interface implemented by this class
     */
    public void addImplements(ReferenceType interfaceReference){
        interfaces.add(interfaceReference);
    }

    /**
     * Adds a {@link GenericityAttribute} to this class which in turn means that this class is generic.
     * If a previous {@link GenericityAttribute} was set in this class, then an exception will be thrown
     *
     * @see #isGeneric()
     * @param generic a {@link GenericityAttribute} which will be added to this class
     * @throws SemanticException if the class was already associated with a generic type
     */
    public void add(GenericityAttribute generic) throws SemanticException{
        if (this.generic != null)
            throw new SemanticException("Una clase no puede tener mas de un tipo generico", generic.getToken());
        this.generic = generic;
    }

    /**
     * Adds a {@link ConstructorSymbol} to this class.
     * If another constructor was previously set, then an exception will be thrown
     *
     * @param constructor a {@link ConstructorSymbol} which will be added to this class
     * @throws SemanticException if the class already had a {@link ConstructorSymbol}
     */
    public void add(ConstructorSymbol constructor) throws SemanticException{
        if (this.constructor != null)
            throw new SemanticException("Las clases solo pueden tener un unico constructor", constructor.getClassReference().getToken());
        this.constructor = constructor;
    }

    /**
     * Adds a {@link MethodSymbol} as a member of this class.
     * If a method with the same name was already added to this class, then an exception will be thrown
     *
     * @param method a {@link MethodSymbol} which will be added as a memeber of this class
     * @throws SemanticException if the class already had a {@link MethodSymbol} with the same name
     */
    public void add(MethodSymbol method) throws SemanticException{
        NameAttribute methodName = method.getNameAttribute();
        if (methods.containsKey(methodName.getValue()))
            throw new SemanticException("Una clase no puede tener dos metodos con el mismo nombre", methodName.getToken());
        methods.put(methodName.getValue(), method);
    }

    /**
     * Adds a {@link AttributeSymbol} as a member of this class.
     * If an attribute with the same name was already added to this class, then an exception will be thrown
     *
     * @param attribute a {@link AttributeSymbol} which will be added as a memeber of this class
     */
    public void add(AttributeSymbol attribute) throws SemanticException{
        NameAttribute attributeName = attribute.getNameAttribute();
        if (attributes.containsKey(attributeName.getValue()))
            throw new SemanticException("Una clase no puede tener dos atributos con el mismo nombre", attributeName.getToken());
        attributes.put(attributeName.getValue(), attribute);
    }

    /**
     * @return the {@link NameAttribute} of this class which contains the name of it
     */
    public NameAttribute getNameAttribute() {
        return name;
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    /**
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of this class
     * which describes the genericity of it
     */
    public Optional<GenericityAttribute> getGeneric() {
        return Optional.ofNullable(generic);
    }

    /**
     * @see #getGeneric()
     * @return true if this class is generic and so has a {@link GenericityAttribute}, false otherwise
     */
    public boolean isGeneric(){
        return generic != null;
    }

    /**
     * @return the {@link ConstructorSymbol} of this class
     */
    public ConstructorSymbol getConstructor() {
        return constructor;
    }

    /**
     * @return a collection of all the {@link AttributeSymbol} of this class
     */
    public Collection<AttributeSymbol> getAttributes() {
        return attributes.values();
    }

    /**
     * @return a collection of all the {@link MethodSymbol} of this class
     */
    public Collection<MethodSymbol> getMethods() {
        return methods.values();
    }

    /**
     * @return an {@link Optional} wrapping a {@link ReferenceType} pointing to the class from which this class extends
     */
    public Optional<ReferenceType> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * @return a collection of {@link ReferenceType} pointing to all the interfaces implemented by this class
     */
    public Collection<ReferenceType> getInterfaces() {
        return interfaces;
    }

    @Override
    public void consolidate() throws SemanticException {
        constructor.consolidate();
        attributes.values().forEach(AttributeSymbol::consolidate);
        methods.values().forEach(MethodSymbol::consolidate);
    }
}
