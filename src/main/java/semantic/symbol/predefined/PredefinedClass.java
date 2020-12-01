package semantic.symbol.predefined;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.*;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;

/**
 * A predefined class is inserted into the {@link semantic.symbol.SymbolTable} at initialization of the program.
 * It defines the name and methods that will already exist at the beginning, so that classes and methods from
 * a source code can call them of make references to them.
 */
public class PredefinedClass implements ClassSymbol {

    private final NameAttribute name;
    private final List<PredefinedMethod> methods = new ArrayList<>();

    private ReferenceType parent;

    public PredefinedClass(String name){
        this.name = NameAttribute.predefined(name);
    }

    /**
     * Adds a new {@link PredefinedMethod} to this predefined class.
     *
     * @param method a {@link PredefinedMethod} which will be added to this predefined class
     */
    public void add(PredefinedMethod method){
        methods.add(method);
    }

    /**
     * Sets the parent class of this predefined class.
     * This parent should be another {@link PredefinedClass}, but this won't be verified.
     *
     * @param reference a {@link ReferenceType} that points to the parent of this predefined class
     */
    public void setParent(ReferenceType reference){
        parent = reference;
    }

    @Override
    public NameAttribute getNameAttribute() {
        return null;
    }

    public String getName(){
        return name.getValue();
    }

    @Override
    public Token getNameToken() {
        return name.getToken();
    }

    @Override
    public Optional<ConstructorSymbol> getConstructor() {
        return Optional.empty();
    }

    @Override
    public Collection<AttributeSymbol> getAttributes() {
        return Collections.emptyList();
    }

    @Override
    public Optional<GenericityAttribute> getGeneric() {
        return Optional.empty();
    }

    @Override
    public Collection<ReferenceType> getParents() {
        if (parent != null){
            return Collections.singleton(parent);
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<MethodSymbol> getMethods(){
        return Collections.unmodifiableList(methods);
    }

    @Override
    public Optional<ReferenceType> getParentClass() {
        return Optional.empty();
    }

    @Override
    public Collection<ReferenceType> getInterfaces() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, AttributeSymbol> inheritAttributes() throws SemanticException {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, MethodSymbol> inheritMethods() throws SemanticException {
        Map<String, MethodSymbol> resultMap;
        ClassSymbol parentSym = null;
        if (parent != null){
            parentSym = SymbolTable.getInstance().getPredefinedClass(parent)
            .orElseThrow(() -> new IllegalStateException("The predefined class has a parent that does not exists"));
        }
        resultMap = parentSym != null ? new HashMap<>(parentSym.inheritMethods()) : new HashMap<>();
        for (PredefinedMethod method : methods) {
            resultMap.put(method.getName(), method);
        }
        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(boolean isPublic, boolean isStatic, NameAttribute name) {
        return Optional.empty();
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(boolean isPublic, boolean isStatic, String name) {
        return Optional.empty();
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(NameAttribute name) {
        return Optional.empty();
    }

    @Override
    public Optional<AttributeSymbol> getAttribute(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<MethodSymbol> getMethod(NameAttribute name) {
        return Optional.empty();
    }

    @Override
    public Optional<MethodSymbol> getMethod(String name){
        return methods.stream()
                .filter(method -> name.equals(method.getName()))
                .map(method -> (MethodSymbol) method)
                .findFirst();
    }

    @Override
    public Optional<MethodSymbol> getMethod(boolean isStatic, NameAttribute name) {
        return methods.stream()
                .filter(method -> name.getValue().equals(method.getName()))
                .filter(method -> method.isStatic().equals(isStatic))
                .map(method -> (MethodSymbol) method)
                .findFirst();
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        // Do nothing
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {
        // Do nothing
    }
}
