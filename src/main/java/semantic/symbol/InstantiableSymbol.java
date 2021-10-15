package semantic.symbol;

public interface InstantiableSymbol<T extends InstantiableSymbol<T>> extends InnerLevelSymbol {

    /**
     * Returns a copy of this symbol but with the generic type of the <code>container</code> instantiated
     * to the given <code>newType</code>.
     *
     * @param container the {@link ClassSymbol} that contains this instantiable symbol
     * @param newType the name of the generic instance
     * @return a copy of this symbol instantiated with the given <code>newType</code>
     * @throws IllegalStateException if the {@link ClassSymbol} hasn't been set yet
     */
    T instantiate(ClassSymbol container, String newType);

}
