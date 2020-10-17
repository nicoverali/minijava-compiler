package syntactic;

import lexical.analyzer.LexicalSequence;
import lexical.Token;

public interface SyntacticAnalyzer {

    /**
     * Analyzes if the associated {@link LexicalSequence} of {@link Token} can be generated
     * with the syntax rules of this parser.
     * <br>
     * If the sequence cannot be generated, then a syntactic error is caught
     * and therefore a {@link SyntacticException} is thrown.
     * <br><br>
     * After an exception is thrown, the next call to this method will continue with the Tokens after
     * the Token that caused the exception.
     *
     * @throws SyntacticException if a syntactic error is detected
     */
    void analyze() throws SyntacticException;

}
