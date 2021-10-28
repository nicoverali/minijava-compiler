package semantic.symbol.attribute;

import lexical.Token;

import java.util.Objects;

/**
 * This attribute determines the name of a Symbol
 */
public class NameAttribute implements SymbolAttribute<String> {

    private Token token;
    private String name;

    public static NameAttribute of(Token token){
        return new NameAttribute(token);
    }

    public static NameAttribute predefined(String name){
        return new NameAttribute(name);
    }

    private NameAttribute(String name){
        this.name = name;
    }

    private NameAttribute(Token tokenName){
        token = tokenName;
        name = tokenName.getLexeme();
    }

    @Override
    public Token getToken() {
        return token;
    }

    /**
     * @return the name of the Symbol as a {@link String}
     */
    @Override
    public String getValue() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameAttribute that = (NameAttribute) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
