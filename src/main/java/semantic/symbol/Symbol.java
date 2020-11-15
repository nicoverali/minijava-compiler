package semantic.symbol;

import semantic.SemanticException;
import semantic.symbol.attribute.NameAttribute;

public interface Symbol {

    /**
     * @return the name of this symbol as a {@link String}
     */
    String getName();

    /**
     * @return the {@link NameAttribute} of this symbol
     */
    NameAttribute getNameAttribute();

    /**
     * Verifies that the symbol declaration is valid. This method should be execute once the {@link SymbolTable}
     * is complete, otherwise it may not work correctly.
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this validation
     */
    void checkDeclaration() throws SemanticException, IllegalStateException;

    /**
     * Makes the symbol fulfill its information related to other symbols, like inherit methods, generics
     * instantiation, etc.
     *
     * @throws SemanticException if a semantic error is detected during consolidation
     * @throws IllegalStateException if the symbol or the {@link SymbolTable} is not ready to make this consolidation
     */
    void consolidate() throws SemanticException, IllegalStateException;

}
