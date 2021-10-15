package semantic.symbol.attribute.type;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.GenericityAttribute;

import java.util.Optional;

/**
 * A {@link ReferenceType} is a {@link Type} which can have genericity. This means that it's compose of another
 * {@link GenericityAttribute} that determines the genericity of it
 */
public class ReferenceType extends Type{

    private GenericityAttribute generic;

    public ReferenceType(String name, String generic){
        super(null, name);
        this.generic = new GenericityAttribute(generic);
    }

    public ReferenceType(String name){
        super(null, name);
    }

    public ReferenceType(Token token) {
        super(token, token.getLexeme());
    }

    public ReferenceType(Token token, GenericityAttribute generic){
        super(token, token.getLexeme());
        this.generic = generic;
    }

    /**
     * Adds a {@link GenericityAttribute} to this type, which in turn converts this type into a generic type.
     * If the type already had a {@link GenericityAttribute} then an exception will be thrown
     *
     * @param generic a {@link GenericityAttribute} which will be added to this reference type
     * @throws SemanticException if the type already had a {@link GenericityAttribute}
     */
    public void addGeneric(GenericityAttribute generic) throws SemanticException {
        if (this.generic != null)
            throw new SemanticException("Las referencias solo pueden tener un unico tipo generico", generic);
        this.generic = generic;
    }

    /**
     * @see #addGeneric(GenericityAttribute)
     * @return true if this reference type is generic and so has a {@link GenericityAttribute}, false otherwise
     */
    public boolean hasGeneric(){
        return generic != null;
    }

    /**
     * @see #hasGeneric() 
     * @return an {@link Optional} wrapping the {@link GenericityAttribute} of this reference type,
     * which determines the genericity of it
     */
    public Optional<GenericityAttribute> getGeneric(){
        return Optional.ofNullable(generic);
    }

    /**
     * Returns a copy of this reference but with the generic type of the <code>container</code> instantiated
     * to the given <code>newType</code>.
     *
     * @param container the container {@link ClassSymbol} of this reference
     * @param newType the name of the generic instance
     * @return a copy of this reference instantiated with the given <code>newType</code>
     */
    public ReferenceType instantiate(ClassSymbol container, String newType) {
        if (container.getGeneric().isPresent()){
            String containerGen = container.getGeneric().get().getValue();
            if (name.equals(containerGen)){
                if (generic != null) throw new SemanticException("El tipo "+name+" no es parametrizable", this);
                return new ReferenceType(newType);
            } else if (generic != null && generic.getValue().equals(containerGen)) {
                return new ReferenceType(name, newType);
            }
        }
        return this;
    }

    @Override
    public void validate(SymbolTable st, ClassSymbol container) throws SemanticException {
        boolean referenceExists = checkInSymbolTable(st, container) || checkInClassContainer(container);
        if (!referenceExists){
            throw new SemanticException("El simbolo al que se hace referencia no pudo ser encontrado.", this.token);
        }
    }

    private boolean checkInSymbolTable(SymbolTable st, ClassSymbol container){
        Optional<ClassSymbol> symbol = st.getClass(this);
        if(symbol.isPresent()){
            checkSymbolGenericity(st, symbol.get(), container);
            return true;
        } else {
            return false;
        }
    }

    private void checkSymbolGenericity(SymbolTable st, ClassSymbol symbol, ClassSymbol container) {
        Optional<GenericityAttribute> generic = symbol.getGeneric();
        if (generic.isPresent() && this.generic == null) {
            throw new SemanticException("Se hace referencia a un simbolo generico sin indicar el tipo generico", this.token);
        } else if (generic.isPresent()) {
            this.generic.validate(st, container);
        } else if (this.generic != null) {
            throw new SemanticException("Se declaro un tipo generico en una referencia a un simbolo no generico", this.generic.getToken());
        }
    }

    private boolean checkInClassContainer(ClassSymbol container){
        boolean isRefToContainerGen = container.getGeneric()
                .map(GenericityAttribute::getValue)
                .map(gen -> gen.equals(this.name))
                .orElse(false);

        if (isRefToContainerGen && generic != null){
            // TODO Si ponemos multiples tipos genericos que ademas pueden ser genericos a su vez, esto tiene que cambiar
            throw new SemanticException("El tipo "+name+" no es parametrizado", this);
        }

        return isRefToContainerGen;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ReferenceType && equals((ReferenceType) obj);
    }

    public boolean equals(ReferenceType type) {
        if (this.generic != null && type.generic != null){
            return this.name.equals(type.name) && this.generic.equals(type.generic);
        } else if (this.generic == null && type.generic == null){
            return this.name.equals(type.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (generic != null){
            return (name+generic.getValue()).hashCode();
        }
        return name.hashCode();
    }
}
