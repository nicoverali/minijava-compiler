package semantic.symbol.user;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.*;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;

public class UserClassSymbol implements ClassSymbol {

    private static final SymbolTable ST = SymbolTable.getInstance();

    private final NameAttribute name;
    private GenericityAttribute generic;

    private ConstructorSymbol constructor;
    private final Map<String, AttributeSymbol> attributes = new HashMap<>();
    private final Map<String, MethodSymbol> methods = new HashMap<>();

    private ReferenceType parent;
    private final List<ReferenceType> interfaces = new ArrayList<>();

    private Map<String, AttributeSymbol> inheritedAttributes;
    private Map<String, MethodSymbol> inheritedMethods;

    public UserClassSymbol(NameAttribute name){
        this.name = name;
        this.parent = ST.getDefaultClass();
    }

    /**
     * Adds a {@link ReferenceType} pointing to a class from which this class extends.
     * If this class already extended another class, then an exception will be thrown
     *
     * @param classReference a {@link ReferenceType} pointing to a class extended by this class
     * @throws SemanticException if the class was already extending another class
     */
    public void setParent(ReferenceType classReference) throws SemanticException {
        if (parent != ST.getDefaultClass()){
            throw new SemanticException("Las clases solo pueden extender una unica clase", classReference);
        } else if (classReference.getValue().equals(this.name.getValue())){
            throw new SemanticException("Una clase no puede extenderse a si misma", classReference);
        }
        parent = classReference;
    }

    /**
     * Adds a {@link ReferenceType} pointing to an interface which this class implements
     *
     * @param interfaceReference a {@link ReferenceType} pointing to an interface implemented by this class
     */
    public void addImplements(ReferenceType interfaceReference){
        if (interfaces.contains(interfaceReference)) {
            throw new SemanticException("No se pude implementar multiples veces la misma interfaz", interfaceReference);
        }
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
            throw new SemanticException("Una clase no puede tener mas de un tipo generico", generic);
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
            throw new SemanticException("Las clases solo pueden tener un unico constructor", constructor.getClassReference());
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
        if (methods.containsKey(method.getName()))
            throw new SemanticException("Una clase no puede tener dos metodos con el mismo nombre", method);
        methods.put(method.getName(), method);
    }

    /**
     * Adds a {@link AttributeSymbol} as a member of this class.
     * If an attribute with the same name was already added to this class, then an exception will be thrown
     *
     * @param attribute a {@link AttributeSymbol} which will be added as a memeber of this class
     */
    public void add(AttributeSymbol attribute) throws SemanticException{
        if (attributes.containsKey(attribute.getName()))
            throw new SemanticException("Una clase no puede tener dos atributos con el mismo nombre", attribute);
        attributes.put(attribute.getName(), attribute);
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
    public Token getNameToken() {
        return name.getToken();
    }

    /**
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of this class
     * which describes the genericity of it
     */
    public Optional<GenericityAttribute> getGeneric() {
        return Optional.ofNullable(generic);
    }

    @Override
    public Collection<ReferenceType> getParents() {
        return Collections.singleton(parent);
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

    @Override
    public Collection<AttributeSymbol> getAttributes() {
        return attributes.values();
    }

    @Override
    public Collection<MethodSymbol> getMethods() {
        return methods.values();
    }

    @Override
    public Optional<ReferenceType> getParentClass() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Collection<ReferenceType> getInterfaces() {
        return interfaces;
    }

    @Override
    public Map<String, AttributeSymbol> inheritAttributes() throws SemanticException {
        if (inheritedAttributes == null) this.inheritMembers();

        Map<String, AttributeSymbol> resultMap = new HashMap<>(inheritedAttributes);
        attributes.forEach(resultMap::put);
        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public Map<String, MethodSymbol> inheritMethods() throws SemanticException {
        if (inheritedMethods == null)  this.inheritMembers();

        Map<String, MethodSymbol> resultMap = new HashMap<>(inheritedMethods);
        methods.forEach(resultMap::put);
        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(NameAttribute name) {
        return getAttribute(name.getValue());
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(String name) {
        AttributeSymbol attr = attributes.get(name);
        if (attr != null){
            return Optional.of(attr);
        }
        return Optional.ofNullable(inheritedAttributes.get(name));
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(boolean isPublic, boolean isStatic, NameAttribute name) {
        return getAttribute(isPublic, isStatic, name.getValue());
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(boolean isPublic, boolean isStatic, String name) {
        AttributeSymbol attr = attributes.get(name);
        if (attr != null && attr.isPublic().equals(isPublic) && attr.isStatic().equals(isStatic)){
            return Optional.of(attr);
        }
        return Optional.ofNullable(inheritedAttributes.get(name));
    }

    @Override
    public Optional<MethodSymbol> getMethod(NameAttribute name) {
        return getMethod(name.getValue());
    }

    @Override
    public Optional<MethodSymbol> getMethod(String name) {
        MethodSymbol method = methods.get(name);
        if (method != null){
            return Optional.of(method);
        }
        return Optional.ofNullable(inheritedMethods.get(name));
    }

    @Override
    public Optional<MethodSymbol> getMethod(boolean isStatic, NameAttribute name) {
        MethodSymbol method = methods.get(name.getValue());
        if (method != null && method.isStatic().equals(isStatic)){
            return Optional.of(method);
        }
        return Optional.ofNullable(inheritedMethods.get(name.getValue()));
    }


    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        if (constructor != null) constructor.checkDeclaration(this);
        attributes.values().forEach(attr -> attr.checkDeclaration(this));
        methods.values().forEach(method -> method.checkDeclaration(this));
        checkParent();
        checkInterfaces();
    }

    private void checkParent() {
        parent.validate(ST, this);
        if (!ST.isAClass(parent)){
            throw new SemanticException("Una clase solo puede extender otra clase", parent);
        }

    }

    private void checkInterfaces() {
        for (ReferenceType i : interfaces) {
            i.validate(ST, this);
            if (!ST.isAnInterface(i)){
                throw new SemanticException("Una clase solo puede implementar interfaces", i);
            }
        }
    }

    @Override
    public void consolidate() throws SemanticException {
        inheritMembers();
        OverwrittenValidator.validateMethods(inheritedMethods, methods);
        OverwrittenValidator.validateImplementation(interfaces, methods, name);
    }

    private void inheritMembers(){
        inheritedAttributes = InheritHelper.inheritAttributes(this);
        inheritedMethods = InheritHelper.inheritMethods(this);
    }

}
