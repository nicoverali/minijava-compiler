package semantic.symbol.attribute;

import lexical.Token;

/**
 * This attribute determines if a Symbol is public or not
 */
public class IsPublicAttribute implements SymbolAttribute<Boolean> {

    private Token token;
    private boolean isPublic;

    public static IsPublicAttribute createPublic(Token token){
        return new IsPublicAttribute(token, true);
    }

    public static IsPublicAttribute createPrivate(Token token){
        return new IsPublicAttribute(token, false);
    }

    public static IsPublicAttribute defaultAttribute(){
        return new IsPublicAttribute(null, true);
    }



    private IsPublicAttribute(Token token, boolean isPublic) {
        this.token = token;
        this.isPublic = isPublic;
    }

    @Override

    public Token getToken() {
        return token;
    }

    /**
     * @return a {@link Boolean} which is true if the Symbol is public or false if not
     */
    @Override
    public Boolean getValue() {
        return isPublic;
    }

    /**
     * @return if the given object is a {@link IsPublicAttribute} and has the same value as this one
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof IsPublicAttribute && equals((IsPublicAttribute) obj);
    }

    public boolean equals(IsPublicAttribute attribute){
        return this.isPublic == attribute.isPublic;
    }
}
