package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.Lexeme;
import lexical.automata.branch.filter.DigitFilter;
import lexical.automata.branch.filter.LexicalFilter;
import lexical.automata.node.strategy.MaxDigitStrategy;

import java.util.Optional;

public class IntegerLexicalNode extends BaseLexicalNode{

    private static final int MAX_DIGITS = 9;
    private static final LexicalFilter filter = new DigitFilter();
    private final MaxDigitStrategy strategy;

    public IntegerLexicalNode(String errorMsg) {
        super("Matches an integer with a limited number of digits", new MaxDigitStrategy(errorMsg));
        this.strategy = new MaxDigitStrategy(errorMsg);
    }

    @Override
    public Token process(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException {
        Optional<CodeCharacter> nextChar = reader.peek();
        if (nextChar.isEmpty()){
            return strategy.onEndOfFile(reader, currentLexeme);
        }

        int numberOfDigits = 1;
        while (nextChar.isPresent() && filter.test(nextChar.get().getValue())){
            if (++numberOfDigits > MAX_DIGITS){
                strategy.onMaxDigitReached(currentLexeme, nextChar.get());
            }
            updateLexeme(reader, currentLexeme);
            nextChar = reader.peek();
        }

        if (nextChar.isEmpty()) return strategy.onEndOfFile(reader, currentLexeme);
        return strategy.onNoBranchSelected(reader, currentLexeme, nextChar.get());
    }
}
