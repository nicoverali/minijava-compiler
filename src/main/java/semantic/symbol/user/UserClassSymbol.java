package semantic.symbol.user;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.*;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.validators.CircularInheritanceValidator;

import java.util.*;
import java.util.stream.Collectors;

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
        constructor.setTopLevelSymbol(this);
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
        method.setTopLevelSymbol(this);
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
        attribute.setTopLevelSymbol(this);
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
    public Map<String, AttributeSymbol> inheritAttributes() throws SemanticException {
        if (inheritedAttributes == null) this.obtainInheritedAttributesAndMethods();

        Map<String, AttributeSymbol> resultMap = new HashMap<>(inheritedAttributes);
        attributes.forEach(resultMap::put);
        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public Map<String, MethodSymbol> inheritMethods() throws SemanticException {
        if (inheritedMethods == null)  this.obtainInheritedAttributesAndMethods();

        Map<String, MethodSymbol> resultMap = new HashMap<>(inheritedMethods);
        methods.forEach(resultMap::put);
        return Collections.unmodifiableMap(resultMap);
    }

    private void obtainInheritedAttributesAndMethods(){
        ClassSymbol parentSym = ST.getClass(parent.getValue())
                .orElseThrow(() -> new SemanticException("No se pudo encontrar el simbolo", parent));
        inheritedAttributes = parentSym.inheritAttributes();
        inheritedMethods = parentSym.inheritMethods();
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        if (constructor != null) constructor.checkDeclaration();
        attributes.values().forEach(AttributeSymbol::checkDeclaration);
        methods.values().forEach(MethodSymbol::checkDeclaration);
        checkParent();
        checkInterfaces();
        CircularInheritanceValidator.validate(this);
    }

    private void checkParent() {
        parent.validate(ST, this);
        if (!ST.isAClass(parent.getValue())){
            throw new SemanticException("Una clase solo puede extender otra clase", parent);
        }

    }

    private void checkInterfaces() {
        for (ReferenceType i : interfaces) {
            i.validate(ST, this);
            if (!ST.isAnInterface(i.getValue())){
                throw new SemanticException("Una clase solo puede implementar interfaces", i);
            }
        }
    }

    @Override
    public void consolidate() throws SemanticException {
        //consolidateInheritance();
        obtainInheritedAttributesAndMethods();
        checkForOverwrittenMethods();
        checkInterfacesAreActuallyImplemented();
        //consolidateMembers();
    }

    private void checkForOverwrittenMethods(){
        List<String> overwritten = methods.values().stream()
                                            .map(MethodSymbol::getName)
                                            .filter(inheritedMethods::containsKey)
                                            .collect(Collectors.toList());
        for (String methodName : overwritten) {
            MethodSymbol ours = methods.get(methodName);
            MethodSymbol theirs = inheritedMethods.get(methodName);
            if (!ours.equals(theirs)){
                throw new SemanticException("Se sobreescribe un metodo pero no se respeta el encabezado original", ours.getNameAttribute());
            }
        }
    }

    private void checkInterfacesAreActuallyImplemented() {
        Map<String, MethodSymbol> toImplement = new HashMap<>();
        List<InterfaceSymbol> parents = interfaces.stream()
                .map(ref -> ST.getInterface(ref.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());

        for (InterfaceSymbol parent : parents){
            for (Map.Entry<String, MethodSymbol> entry : parent.inheritMethods().entrySet()){
                MethodSymbol overwritten = toImplement.get(entry.getKey());
                if (overwritten != null && !overwritten.equals(entry.getValue())){
                    throw new SemanticException("La clase extiende dos interfaces cuyos metodos colisionan", this.name);
                }
                toImplement.put(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<String, MethodSymbol> entry : toImplement.entrySet()){
            MethodSymbol ours = methods.get(entry.getKey());
            if (ours == null){
                throw new SemanticException("La clase no implementa el metodo "+entry.getValue().getName(), this.name);
            } else if (!ours.equals(entry.getValue())){
                throw new SemanticException("Se implementa el metodo pero no se respeta el encabezado", ours.getNameAttribute());
            }
        }
    }
}
