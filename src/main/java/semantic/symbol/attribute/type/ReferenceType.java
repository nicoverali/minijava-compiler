package semantic.symbol.attribute.type;

import lexical.Token;
import org.jetbrains.annotations.Nullable;
import semantic.SemanticException;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.GenericityAttribute;

import java.util.Optional;

/**
 * A {@link ReferenceType} is a {@link Type} which can have genericity. This means that it's compose of another
 * {@link GenericityAttribute} that determines the genericity of it
 */
public class ReferenceType extends Type{

    private GenericityAttribute generic;

    public ReferenceType(String name){
        super(null, name);
    }

    public ReferenceType(Token token) {
        super(token, token.getLexeme().toString());
    }

    public ReferenceType(Token token, GenericityAttribute generic){
        super(token, token.getLexeme().toString());
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
            throw new SemanticException("Las referencias solo pueden tener un unico tipo generico", generic.getToken());
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
    @Nullable
    public Optional<GenericityAttribute> getGeneric(){
        return Optional.ofNullable(generic);
    }

    @Override
    public void validate(SymbolTable st, TopLevelSymbol container) throws SemanticException {
        boolean referenceExists = checkInSymbolTable(st, container) || checkInTopLevelGenericity(container);
        if (!referenceExists){
            throw new SemanticException("El simbolo al que se hace referencia no pudo ser encontrado.", this.token);
        }
    }

    private boolean checkInSymbolTable(SymbolTable st, TopLevelSymbol container){
        Optional<TopLevelSymbol> symbol = st.getTopLevelSymbol(this.name);
        if(symbol.isPresent()){
            checkSymbolGenericity(st, symbol.get(), container);
            return true;
        } else {
            return false;
        }
    }

    private void checkSymbolGenericity(SymbolTable st, TopLevelSymbol symbol, TopLevelSymbol container) {
        Optional<GenericityAttribute> generic = symbol.getGeneric();
        if (generic.isPresent() && this.generic == null) {
            throw new SemanticException("Se hace referencia a un simbolo generico sin indicar el tipo generico", this.token);
        } else if (generic.isPresent()) {
            this.generic.validate(st, container);
        } else if (this.generic != null) {
            throw new SemanticException("Se declaro un tipo generico en una referencia a un simbolo no generico", this.generic.getToken());
        }
    }

    private boolean checkInTopLevelGenericity(TopLevelSymbol container){
        return container.getGeneric()
                .map(GenericityAttribute::getValue)
                .map(gen -> gen.equals(this.name))
                .orElse(false);
    }
}
