package semantic.symbol;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.user.InheritHelper;
import semantic.symbol.user.OverwrittenValidator;

import java.util.*;

public class InterfaceSymbol implements TopLevelSymbol {

    private static final SymbolTable ST = SymbolTable.getInstance();

    private NameAttribute name;
    private GenericityAttribute generic;

    private Map<String, MethodSymbol> methods = new HashMap<>();
    private List<ReferenceType> extend = new ArrayList<>();

    private Map<String, MethodSymbol> superMethods;

    public InterfaceSymbol(NameAttribute name){
        this.name = name;
    }

    /**
     * Adds a {@link ReferenceType} pointing to an {@link InterfaceSymbol} from which this interface extends.
     *
     * @param extendsReference a {@link ReferenceType} pointing to a {@link InterfaceSymbol}
     *                         which is extended by this interface
     */
    public void addExtends(ReferenceType extendsReference){
        if (extend.contains(extendsReference)){
            throw new SemanticException("No se puede extender multiples veces de la misma interfaz", extendsReference);
        }
        if (extendsReference.getValue().equals(this.name.getValue())){
            throw new SemanticException("Una interfaz no puede extenderse a si misma", extendsReference);
        }
        extend.add(extendsReference);
    }

    /**
     * Adds genericity to this interface.
     * If a previous {@link GenericityAttribute} was set, then an exception will be thrown
     *
     * @param generic a {@link GenericityAttribute} which describes the genericity of this interface
     * @throws SemanticException if a previous {@link GenericityAttribute} was already set
     */
    public void add(GenericityAttribute generic) throws SemanticException {
        if (this.generic != null)
            throw new SemanticException("Una interfaz puede tener un unico tipo generico", generic);
        this.generic = generic;
    }

    /**
     * Adds a new {@link MethodSymbol} to the list of this interface members.
     * If another method with the same name was already a member of this interface, then an exception will be thrown
     *
     * @param method a {@link MethodSymbol} which will be added as a memeber of this interfaceº
     * @throws SemanticException if the interface already had a method with the same name
     */
    public void add(MethodSymbol method) throws SemanticException{
        if (methods.containsKey(method.getName()))
            throw new SemanticException("Una interfaz no puede tener mas de un metodo con el mismo nombre", method);
        methods.put(method.getName(), method);
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
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of this interface
     * which describes the genericity of it
     */
    public Optional<GenericityAttribute> getGeneric() {
        return Optional.ofNullable(generic);
    }

    @Override
    public Collection<ReferenceType> getParents() {
        return extend;
    }

    /**
     * @return true if this interface has genericity and so has a {@link GenericityAttribute}, false otherwise
     */
    public boolean isGeneric(){
        return generic != null;
    }

    /**
     * @return a collection of all the {@link MethodSymbol} of this interface
     */
    public Collection<MethodSymbol> getMethods() {
        return methods.values();
    }

    @Override
    public Map<String, MethodSymbol> inheritMethods(){
        superMethods = superMethods == null
                ? InheritHelper.inheritMethods(this)
                : superMethods;

        Map<String, MethodSymbol> resultMap = new HashMap<>(superMethods);
        methods.forEach(resultMap::put);
        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public Optional<MethodSymbol> getMethod(NameAttribute name) {
        return getMethod(name.getValue());
    }

    @Override
    public Optional<MethodSymbol> getMethod(String name) {
        MethodSymbol method = methods.get(name);
        if (methods != null){
            return Optional.of(method);
        }
        return Optional.ofNullable(superMethods.get(name));
    }

    @Override
    public Optional<MethodSymbol> getMethod(boolean isStatic, NameAttribute name) {
        MethodSymbol method = methods.get(name.getValue());
        if (methods != null && method.isStatic().equals(isStatic)){
            return Optional.of(method);
        }
        return Optional.ofNullable(superMethods.get(name.getValue()));
    }

    @Override
    public void checkDeclaration() throws SemanticException, IllegalStateException {
        methods.values().forEach(method -> method.checkDeclaration(this));
        checkExtensions();
    }

    private void checkExtensions() {
        for (ReferenceType ref : extend) {
            ref.validate(ST, this);
            if (!ST.isAnInterface(ref)){
                throw new SemanticException("Una interfaz solo puede extender interfaces", ref);
            }
        }
    }

    @Override
    public void consolidate() throws SemanticException {
        superMethods = InheritHelper.inheritMethods(this);
        OverwrittenValidator.validateMethods(superMethods, methods);
    }

}
