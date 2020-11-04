package semantic.symbol.attribute.type;

import lexical.Token;
import org.jetbrains.annotations.Nullable;
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
     * Adds a {@link GenericityAttribute} to this type, which in turn converts this type into a generic type
     * @param generic a {@link GenericityAttribute} which will be added to this reference type
     */
    public void addGeneric(GenericityAttribute generic){
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
