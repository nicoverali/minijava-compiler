package semantic.symbol.user;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.*;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import util.map.HashMultimap;
import util.map.HashSetMultimap;
import util.map.Multimap;

import java.util.*;

public class UserClassSymbol implements ClassSymbol {

    private static final SymbolTable ST = SymbolTable.getInstance();

    private final NameAttribute name;

    private final List<ConstructorSymbol> constructors = new ArrayList<>();
    private final Map<String, AttributeSymbol> attributes = new HashMap<>();
    private final Multimap<String, MethodSymbol> methods = new HashMultimap<>();

    private ReferenceType parent;

    private Map<String, AttributeSymbol> inheritedAttributes;
    private Multimap<String, MethodSymbol> inheritedMethods;

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
     * Adds a {@link ConstructorSymbol} to this class.
     * A class cannot have duplicate constructors, that is, constructors with the same arguments
     *
     * @param constructor a {@link ConstructorSymbol} which will be added to this class
     * @throws SemanticException if the class already had a {@link ConstructorSymbol} with the same arguments
     */
    public void add(ConstructorSymbol constructor) throws SemanticException{
        boolean duplicateConstructor = constructors.stream().anyMatch(c -> c.equals(constructor));
        if (duplicateConstructor){
                throw new SemanticException("Constructor duplicado", constructor.getClassReference());
        }
        constructors.add(constructor);
    }

    /**
     * Adds a {@link MethodSymbol} as a member of this class.
     * If a method with the same name was already added to this class, then an exception will be thrown
     *
     * @param method a {@link MethodSymbol} which will be added as a memeber of this class
     * @throws SemanticException if the class already had a {@link MethodSymbol} with the same name
     */
    public void add(MethodSymbol method) throws SemanticException{
        boolean duplicateMethod = methods.get(method.getName()).stream().anyMatch(m -> !m.isValidOverload(method));
        if (duplicateMethod) {
            throw new SemanticException("Metodo duplicado", method);
        }

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

    @Override
    public List<ConstructorSymbol> getConstructors() {
        if (!constructors.isEmpty()) return new ArrayList<>(constructors);
        return List.of(new ConstructorSymbol(new ReferenceType(getName()), List.of()));
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
    public Map<String, AttributeSymbol> getAllAttributes() throws SemanticException {
        if (inheritedAttributes == null) this.inheritMembers();

        Map<String, AttributeSymbol> resultMap = new HashMap<>(inheritedAttributes);
        resultMap.putAll(attributes);
        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public Multimap<String, MethodSymbol> getAllMethods() throws SemanticException {
        if (inheritedMethods == null)  this.inheritMembers();

        HashSetMultimap<String, MethodSymbol> resultMap = new HashSetMultimap<>(inheritedMethods);
        resultMap.putAll(methods);
        return resultMap;
    }


    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        constructors.forEach(cons -> cons.checkDeclaration(this));
        attributes.values().forEach(attr -> attr.checkDeclaration(this));
        methods.values().forEach(method -> method.checkDeclaration(this));
        checkParent();
    }

    private void checkParent() {
        parent.validate(ST);
    }

    @Override
    public void consolidate() throws SemanticException {
        inheritMembers();
        OverwrittenValidator.validateMethods(inheritedMethods, methods);
    }

    private void inheritMembers(){
        inheritedAttributes = InheritHelper.inheritAttributes(this);
        inheritedMethods = InheritHelper.inheritMethods(this);
    }

}
