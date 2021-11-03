package semantic.symbol.attribute;

import lexical.Token;

import java.util.Objects;

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

    public static IsStaticAttribute emptyStatic(){
        return new IsStaticAttribute(null, true);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsStaticAttribute that = (IsStaticAttribute) o;
        return isStatic == that.isStatic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isStatic);
    }

    @Override
    public String toString() {
        return isStatic
                ? "static"
                : "dynamic";
    }
}
