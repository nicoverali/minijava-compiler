package semantic.symbol.attribute.type;

import lexical.Token;
import semantic.SemanticException;
import semantic.symbol.ClassSymbol;
import semantic.symbol.SymbolTable;

import java.util.Optional;

/**
 * A {@link ReferenceType} is a {@link Type} which points to a Class
 */
public class ReferenceType extends Type{

    public ReferenceType(String name){
        super(null, name);
    }

    public ReferenceType(Token token) {
        super(token, token.getLexeme());
    }


    @Override
    public void validate(SymbolTable st) throws SemanticException {
        boolean referenceExists = checkInSymbolTable(st);
        if (!referenceExists){
            throw new SemanticException("El simbolo al que se hace referencia no pudo ser encontrado.", this.token);
        }
    }

    private boolean checkInSymbolTable(SymbolTable st){
        Optional<ClassSymbol> symbol = st.getClass(this);
        return symbol.isPresent();
    }
}
