package lexical.automata.filter;

public interface LexicalFilter {

    /**
     * Checks if the given <i>char</i> value passes the filter or not.
     *
     * @param value <i>char</i> value to test
     * @return true if the given value passes this filter, false otherwise
     */
    boolean test(char value);

}
