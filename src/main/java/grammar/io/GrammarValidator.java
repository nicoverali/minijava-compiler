package grammar.io;

import java.util.Collection;

public interface GrammarValidator {

    /**
     * Verifies that the given term is correct. It should be on the left side of at least one production rule.
     *
     * @param term the term which will be verified
     * @param heads a {@link Collection} of the heads of all the production rules
     * @param lineNumber the line number of the given term
     */
    void validateNonTerminal(String term, Collection<String> heads, int lineNumber);

    /**
     * Verifies that the given term is a valid terminal. It also may map the term name to a new one.
     *
     * @param term the term which will be verified
     * @param lineNumber the line number of the given term
     * @return the new name of the term after being mapped
     */
    String validateTerminal(String term, int lineNumber);
}
