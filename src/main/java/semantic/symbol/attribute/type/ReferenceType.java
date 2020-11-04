package semantic.symbol.attribute.type;

import lexical.Token;
import org.jetbrains.annotations.Nullable;
import semantic.SemanticException;
import semantic.symbol.attribute.GenericityAttribute;

import java.util.Optional;

/**
 * A {@link ReferenceType} is a {@link Type} which can have genericity. This means that it's compose of another
 * {@link GenericityAttribute} that determines the genericity of it
 */
public class ReferenceType extends Type{

    private GenericityAttribute generic;

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

}
