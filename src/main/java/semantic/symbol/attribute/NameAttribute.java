package semantic.symbol.attribute;

import lexical.Token;

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
        name = tokenName.getLexeme().toString();
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

    /**
     * @return true if the given object is a {@link NameAttribute} and has the same name as this one
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof NameAttribute && equals((NameAttribute) obj);
    }

    /**
     * @return true if the given {@link NameAttribute} has the same name as this one, false otherwise
     */
    public boolean equals(NameAttribute attribute){
        return this.name.equals(attribute.name);
    }
}
