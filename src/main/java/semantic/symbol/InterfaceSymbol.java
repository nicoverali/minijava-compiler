package semantic.symbol;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;
import semantic.symbol.user.InheritHelper;
import semantic.symbol.validators.CircularInheritanceValidator;

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
        NameAttribute methodName = method.getNameAttribute();
        if (methods.containsKey(methodName.getValue()))
            throw new SemanticException("Una interfaz no puede tener mas de un metodo con el mismo nombre", methodName);
        method.setTopLevelSymbol(this);
        methods.put(methodName.getValue(), method);
    }

    /**
     * @return the {@link NameAttribute} of this interface which contains the name of it
     */
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
        for (Map.Entry<String, MethodSymbol> entry : methods.entrySet()){
            MethodSymbol overwritten = resultMap.get(entry.getKey());
            if (overwritten != null && !overwritten.equals(entry.getValue())){
                throw new SemanticException("Se sobreescribe el metodo pero no se respeta el encabezado", entry.getValue().getNameAttribute());
            }
            resultMap.put(entry.getKey(), entry.getValue());
        }

        return Collections.unmodifiableMap(resultMap);
    }

    @Override
    public void consolidate() throws SemanticException {
        consolidateInheritance();
        consolidateMembers();
        CircularInheritanceValidator.validate(this);
        superMethods = superMethods == null
                ? InheritHelper.inheritMethods(this)
                : superMethods;
        checkForOverwrittenMethods();
    }

    private void consolidateInheritance() {
        extend.forEach(extend -> extend.validate(ST, this));
        for (ReferenceType ref : extend) {
            if (!ST.isAnInterface(ref.getValue())){
                throw new SemanticException("Una interfaz solo puede extender interfaces", ref);
            }
        }
    }

    private void consolidateMembers() {
        methods.values().forEach(MethodSymbol::consolidate);
    }

    private void checkForOverwrittenMethods() {
        for (Map.Entry<String, MethodSymbol> entry : methods.entrySet()){
            MethodSymbol overwritten = superMethods.get(entry.getKey());
            if (overwritten != null && !overwritten.equals(entry.getValue())){
                throw new SemanticException("Se sobreescribe el metodo pero no se respeta el encabezado", entry.getValue().getNameAttribute());
            }
        }
    }
}
