package semantic.symbol;

import semantic.SemanticException;

public interface InnerLevelSymbol extends Symbol {

    /**
     * Verifies that the symbol declaration is valid. This method should be executed once the {@link SymbolTable}
     * is complete, otherwise it may not work correctly.
     *
     * @param container the {@link ClassSymbol} that contains this inner symbol
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this validation
     */
    void checkDeclaration(ClassSymbol container) throws SemanticException, IllegalStateException;

}
