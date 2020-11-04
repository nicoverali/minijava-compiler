package syntactic.entity.attribute.type;

import lexical.Token;
import org.jetbrains.annotations.Nullable;
import syntactic.entity.attribute.SymbolAttribute;

/**
 * A type is another {@link SymbolAttribute} that represents the type of a symbol.
 */
public abstract class Type implements SymbolAttribute<String> {

    private final Token token;
    private final String name;

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
}
