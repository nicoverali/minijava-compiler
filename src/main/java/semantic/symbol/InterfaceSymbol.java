package semantic.symbol;

import lexical.Token;
import semantic.CircularInheritanceException;
import semantic.SemanticException;
import semantic.symbol.attribute.GenericityAttribute;
import semantic.symbol.attribute.NameAttribute;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.*;
import java.util.stream.Collectors;

public class InterfaceSymbol implements TopLevelSymbol {

    private static final SymbolTable ST = SymbolTable.getInstance();

    private NameAttribute name;
    private GenericityAttribute generic;

    private Map<String, MethodSymbol> methods = new HashMap<>();
    private List<ReferenceType> extend = new ArrayList<>();

    private boolean circularInheritanceCheck = false; // This is use when checking for circular inheritance

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
            throw new SemanticException("Una interfaz no puede extenderse a si misma", extendsReference.getToken());
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
            throw new SemanticException("Una interfaz puede tener un unico tipo generico", generic.getToken());
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
            throw new SemanticException("Una interfaz no puede tener mas de un metodo con el mismo nombre", methodName.getToken());
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

    /**
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of this interface
     * which describes the genericity of it
     */
    public Optional<GenericityAttribute> getGeneric() {
        return Optional.ofNullable(generic);
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

    /**
     * @return a collection of all the {@link InterfaceSymbol} extended by this interface
     */
    public Collection<ReferenceType> getExtend() {
        return extend;
    }

    @Override
    public void consolidate() throws SemanticException {
        consolidateInheritance();
        consolidateMembers();
        checkForCircularInheritance(new ArrayList<>());
    }

    private void consolidateInheritance() {
        extend.forEach(extend -> extend.validate(ST, this));
        for (ReferenceType ref : extend) {
            if (!ST.isAnInterface(ref.getValue())){
                throw new SemanticException("Una interfaz solo puede extender interfaces", ref.getToken());
            }
        }
    }

    private void checkForCircularInheritance(List<InterfaceSymbol> visited){
        if (circularInheritanceCheck){
            return;
        } else if (visited.contains(this)){
            List<Token> involved = visited.stream().map(i -> i.name.getToken()).collect(Collectors.toList());
            throw new CircularInheritanceException("La interfaz "+name.getValue()+ " sufre de herencia circular", involved);
        }

        visited.add(this);
        extend.stream()
                .map(ReferenceType::getValue)
                .map(ST::getInterface)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(i -> i.checkForCircularInheritance(new ArrayList<>(visited)));
        circularInheritanceCheck = true;
    }

    private void consolidateMembers() {
        methods.values().forEach(MethodSymbol::consolidate);
    }
}
