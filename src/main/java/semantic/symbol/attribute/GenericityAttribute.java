package semantic.symbol.attribute;

import lexical.Token;

/**
 * This attribute determines the genericity of a Symbol
 */
public class GenericityAttribute implements SymbolAttribute<String> {

    private Token token;
    private String name;

    public GenericityAttribute(Token token){
        this.token = token;
        name = token.getLexeme().toString();
    }

    @Override
    public Token getToken() {
        return token;
    }

    /**
     * @return the name of the generic type as a {@link String}
     */
    @Override
    public String getValue() {
        return name;
    }
}
