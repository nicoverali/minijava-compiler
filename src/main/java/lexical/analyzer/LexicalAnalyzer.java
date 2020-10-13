package lexical.analyzer;

import lexical.LexicalException;
import lexical.Token;

import java.util.Optional;

public interface LexicalAnalyzer {

    /**
     * Takes a source code as input and generates one by one the {@link Token}s
     * present in the file.
     * <br>
     * The last {@link Token} will always be EOF, after which
     * it will return an empty {@link Optional}
     *
     * @return an {@link Optional} wrapping the next {@link Token} in the source code
     * @throws LexicalException if the analyzer detects a lexical error
     */
    Optional<Token> getNextToken() throws LexicalException;

}
