package semantic.symbol.attribute;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.attribute.type.ReferenceType;

import java.util.Optional;

/**
 * This attribute determines the genericity of a Symbol
 */
// TODO Esto hay que revisarlo entero, si vamos a implementar genericidad, es muy distinta la nueva implementacion
public class GenericityAttribute implements SymbolAttribute<String> {

    private final Token token;
    private final String name;

    public GenericityAttribute(Token token){
        this.token = token;
        name = token.getLexeme();
    }

    public GenericityAttribute(String name){
        token = null;
        this.name = name;
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
    public void validate(SymbolTable st, ClassSymbol container) throws SemanticException {
        boolean isValid = isATopLevelSymbol(st) || isGenericTypeOfContainer(container);

        if (!isValid){
            throw new SemanticException("No es un tipo generico valido", this.token);
        }
    }

    private boolean isATopLevelSymbol(SymbolTable st){
        Optional<ClassSymbol> symbol = st.getClass(new ReferenceType(this.name));
        if (symbol.isPresent() && symbol.get().getGeneric().isPresent()){
            throw new SemanticException("Un tipo generico no se puede instanciar con un simbolo generico. Genericidad recursiva", this);
        }
        return symbol.isPresent();
    }

    private boolean isGenericTypeOfContainer(ClassSymbol container){
        return container.getGeneric()
                .map(gen -> gen.name.equals(this.name))
                .orElse(false);
    }

    /**
     * @return true if the given object is a {@link GenericityAttribute} and has the same value as this one
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof GenericityAttribute && equals((GenericityAttribute) obj);
    }

    /**
     * @return true if the given {@link GenericityAttribute} has the same value as this one
     */
    public boolean equals(GenericityAttribute attribute){
        return this.name.equals(attribute.name);
    }
}
