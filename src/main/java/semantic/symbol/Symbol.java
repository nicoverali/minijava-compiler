package semantic.symbol;

import semantic.SemanticException;

public interface Symbol {

    /**
     * Verifies that the symbol declaration is valid. This method should be execute once the {@link SymbolTable}
     * is complete, otherwise it may not work correctly.
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol is not even ready to try consolidating
     */
    void consolidate() throws SemanticException, IllegalStateException;

}
