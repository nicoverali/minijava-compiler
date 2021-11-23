package semantic.symbol;

import semantic.SemanticException;

import javax.annotation.Nullable;

public interface InnerClassSymbol extends Symbol {

    /**
     * Verifies that the symbol declaration is valid. This method should be executed once the {@link SymbolTable}
     * is complete, otherwise it may not work correctly.
     * Make sure to set the container of this symbol before calling this method
     *
     * @see #setContainer(ClassSymbol)
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this validation
     */
    void checkDeclaration() throws SemanticException, IllegalStateException;

    /**
     * Sets the container for this {@link InnerClassSymbol}
     *
     * @param container the {@link ClassSymbol} that contains this symbol
     */
    void setContainer(ClassSymbol container);

    /**
     * @return the {@link ClassSymbol} that contains this symbol or null if it is un-contained
     */
    @Nullable
    ClassSymbol getContainer();

}
