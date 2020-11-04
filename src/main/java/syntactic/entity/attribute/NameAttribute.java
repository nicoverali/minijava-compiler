package syntactic.entity.attribute;

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
}
