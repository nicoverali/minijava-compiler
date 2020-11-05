package semantic.symbol;

public interface InnerLevelSymbol extends Symbol {

    /**
     * Sets the given {@link TopLevelSymbol} as the container of this symbol
     *
     * @param symbol the {@link TopLevelSymbol} that contains this symbol
     */
    void setTopLevelSymbol(TopLevelSymbol symbol);

}
