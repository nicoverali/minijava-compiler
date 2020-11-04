package semantic.symbol.attribute;

import lexical.Token;

/**
 * This attribute determines if an Symbol is static or not
 */
public class IsStaticAttribute implements SymbolAttribute<Boolean>{

    private final Token token;
    private final boolean isStatic;

    public static IsStaticAttribute createStatic(Token token){
        return new IsStaticAttribute(token, true);
    }

    public static IsStaticAttribute createDynamic(Token token){
        return new IsStaticAttribute(token, false);
    }

    public static IsStaticAttribute emptyDynamic(){
        return new IsStaticAttribute(null, false);
    }

    public static IsStaticAttribute defaultAttribute(){
        return new IsStaticAttribute(null, false);
    }

    private IsStaticAttribute(Token token, boolean isStatic){
        this.token = token;
        this.isStatic = isStatic;
    }

    @Override
    public Token getToken() {
        return token;
    }

    /**
     * @return a {@link Boolean} which is true if the Symbol is static or false if not
     */
    @Override
    public Boolean getValue() {
        return isStatic;
    }
}
