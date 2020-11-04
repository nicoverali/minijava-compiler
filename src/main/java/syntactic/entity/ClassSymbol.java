package syntactic.entity;

import syntactic.entity.attribute.GenericityAttribute;
import syntactic.entity.attribute.NameAttribute;
import syntactic.entity.attribute.type.ReferenceType;

import java.util.*;

public class ClassSymbol {

    private final NameAttribute name;
    private GenericityAttribute generic;

    private final List<ConstructorSymbol> constructors = new ArrayList<>();
    private final Map<String, AttributeSymbol> attributes = new HashMap<>();
    private final Map<String, MethodSymbol> methods = new HashMap<>();

    private ReferenceType parent;
    private final List<ReferenceType> interfaces = new ArrayList<>();

    public ClassSymbol(NameAttribute name){
        this.name = name;
    }

    /**
     * Adds a {@link ReferenceType} pointing to a class from which this class extends.
     * If this class already extended another class, then the original {@link ReferenceType} will be replaced
     *
     * @param classReference a {@link ReferenceType} pointing to a class extended by this class
     */
    public void addExtends(ReferenceType classReference){
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
     * If a previous {@link GenericityAttribute} was set in this class, then it will be replaced.
     *
     * @see #isGeneric()
     * @param generic a {@link GenericityAttribute} which will be added to this class
     */
    public void add(GenericityAttribute generic){
        this.generic = generic;
    }

    /**
     * Adds a {@link ConstructorSymbol} to this class.
     *
     * @param constructor a {@link ConstructorSymbol} which will be added to this class
     */
    public void add(ConstructorSymbol constructor){
        constructors.add(constructor);
    }

    /**
     * Adds a {@link MethodSymbol} as a member of this class.
     *
     * @param method a {@link MethodSymbol} which will be added as a memeber of this class
     */
    public void add(MethodSymbol method){
        methods.put(method.getName().getValue(), method);
    }

    /**
     * Adds a {@link AttributeSymbol} as a member of this class
     *
     * @param attribute a {@link AttributeSymbol} which will be added as a memeber of this class
     */
    public void add(AttributeSymbol attribute){
        attributes.put(attribute.getName().getValue(), attribute);
    }

    /**
     * @return the {@link NameAttribute} of this class which contains the name of it
     */
    public NameAttribute getName() {
        return name;
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
     * @return a collection of all the {@link ConstructorSymbol} of this class
     */
    public Collection<ConstructorSymbol> getConstructors() {
        return constructors;
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
}
