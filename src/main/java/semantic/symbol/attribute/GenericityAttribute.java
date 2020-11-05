package semantic.symbol.attribute;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;

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

    /**
     * Verifies that this generic attribute is valid.
     *
     * @throws SemanticException if a semantic error is detected
     */
    public void validate(SymbolTable st, TopLevelSymbol container) throws SemanticException {
        boolean isValid = isATopLevelSymbol(st) || isGenericTypeOfContainer(container);

        if (!isValid){
            throw new SemanticException("No es un tipo generico valido", this.token);
        }
    }

    private boolean isATopLevelSymbol(SymbolTable st){
        return st.getTopLevelSymbol(this.name).isPresent();
    }

    private boolean isGenericTypeOfContainer(TopLevelSymbol container){
        return container.getGeneric()
                .map(gen -> gen.name.equals(this.name))
                .orElse(false);
    }
}
