package syntactic.entity;

import org.jetbrains.annotations.Nullable;
import syntactic.entity.attribute.GenericityAttribute;
import syntactic.entity.attribute.NameAttribute;
import syntactic.entity.attribute.type.ReferenceType;

import java.util.*;

public class InterfaceSymbol {

    private NameAttribute name;
    private GenericityAttribute generic;

    private Map<String, MethodSymbol> methods = new HashMap<>();
    private List<ReferenceType> extend = new ArrayList<>();

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
        extend.add(extendsReference);
    }

    /**
     * Adds genericity to this interface. If a previous {@link GenericityAttribute} was set, then it will be replaced
     *
     * @param generic a {@link GenericityAttribute} which describes the genericity of this interface
     */
    public void add(GenericityAttribute generic){
        this.generic = generic;
    }

    /**
     * Adds a new {@link MethodSymbol} to the list of this interface members.
     *
     * @param method a {@link MethodSymbol} which will be added as a memeber of this interfaceº
     */
    public void add(MethodSymbol method){
        methods.put(method.getName().getValue(), method);
    }

    /**
     * @return the {@link NameAttribute} of this interface which contains the name of it
     */
    public NameAttribute getName() {
        return name;
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
}
