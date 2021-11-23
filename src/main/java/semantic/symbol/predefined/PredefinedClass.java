package semantic.symbol.predefined;

import lexical.Token;
import semantic.SemanticException;
import semantic.ast.block.BlockNode;
import semantic.symbol.*;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import util.map.HashMultimap;
import util.map.HashSetMultimap;
import util.map.Multimap;

import java.util.*;

/**
 * A predefined class is inserted into the {@link semantic.symbol.SymbolTable} at initialization of the program.
 * It defines the name and methods that will already exist at the beginning, so that classes and methods from
 * a source code can call them of make references to them.
 */
public class PredefinedClass implements ClassSymbol {

    private final NameAttribute name;
    private final Multimap<String, MethodSymbol> methods = new HashMultimap<>();
    private Multimap<String, MethodSymbol> inheritMethods;

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
        method.setContainer(this);
        methods.put(method.getName(), method);
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
    public Collection<AttributeSymbol> getAttributes() {
        return Collections.emptyList();
    }

    @Override
    public Collection<AttributeSymbol> getInheritAttributes() {
        return Collections.emptyList();
    }

    @Override
    public Collection<MethodSymbol> getMethods(){
        return methods.values();
    }

    @Override
    public Optional<ReferenceType> getParentClass() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Collection<ConstructorSymbol> getConstructors() {
        return List.of(
                new ConstructorSymbol(
                        new ReferenceType(name.getValue()),
                        List.of(),
                        BlockNode.empty()
                )
        );
    }

    @Override
    public Map<String, AttributeSymbol> getAllAttributes() throws SemanticException {
        return Collections.emptyMap();
    }

    @Override
    public Multimap<String, MethodSymbol> getAllMethods() throws SemanticException {
        if (inheritMethods == null) inheritMethods();

        for (MethodSymbol method : methods.values()) {
            inheritMethods.put(method.getName(), method);
        }
        return inheritMethods;
    }

    private void inheritMethods() {
        ClassSymbol parentSym = null;
        if (parent != null){
            parentSym = SymbolTable.getInstance().getPredefinedClass(parent)
                    .orElseThrow(() -> new IllegalStateException("The predefined class has a parent that does not exists"));
        }
        inheritMethods = parentSym != null ? new HashSetMultimap<>(parentSym.getAllMethods()) : new HashSetMultimap<>();
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        // Do nothing
    }

    @Override
    public void consolidate() throws SemanticException, IllegalStateException {
        if (inheritMethods == null) inheritMethods();
    }

    @Override
    public void checkSentences() throws SemanticException, IllegalStateException {
        // Predefined classes don't have sentences
    }
}
