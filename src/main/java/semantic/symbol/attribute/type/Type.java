package semantic.symbol.attribute.type;

import lexical.Token;
import org.jetbrains.annotations.Nullable;
import semantic.SemanticException;
import semantic.symbol.SymbolTable;
import semantic.symbol.TopLevelSymbol;
import semantic.symbol.attribute.SymbolAttribute;

/**
 * A type is another {@link SymbolAttribute} that represents the type of a symbol.
 */
public abstract class Type implements SymbolAttribute<String> {

    protected final Token token;
    protected final String name;

    public Type(Token token, String name) {
        this.token = token;
        this.name = name;
    }

    @Override
    @Nullable
    public Token getToken() {
        return token;
    }

    /**
     * @return the name of the type as a {@link String}
     */
    @Override
    public String getValue() {
        return name;
    }

    /**
     * Verifies that this type is a valid type according to the given {@link SymbolTable} and
     * within the context of the given {@link TopLevelSymbol}
     *
     * @param st a {@link SymbolTable} to check if this type is valid
     * @param topSymbol  a {@link TopLevelSymbol} where this type was declared
     * @throws SemanticException if this type is not valid and a semantic error is detected
     */
    public void validate(SymbolTable st, TopLevelSymbol topSymbol) throws SemanticException {
        // By default every type is valid
    }
}
