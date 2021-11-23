package semantic.symbol;

import semantic.SemanticException;
import semantic.Variable;
import semantic.symbol.attribute.type.Type;

public interface ParameterSymbol extends Symbol, Variable {

    /**
     * @return the {@link Type} associated with this parameter
     */
    Type getType();

    /**
     * Verifies that the parameter declaration is valid. This method should be executed once the {@link SymbolTable}
     * is complete, otherwise it may not work correctly.
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this validation
     */
    void checkDeclaration(ClassSymbol container) throws SemanticException, IllegalStateException;
}
