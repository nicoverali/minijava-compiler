package lexical.analyzer;

import lexical.Token;
import sequence.Sequence;

import java.util.Optional;

/**
 * This class is {@link Sequence} abstraction over the {@link Token} emitted by a {@link LexicalAnalyzer}.
 * This allows clients to {@link #peek()} tokens without consuming them, and thus take decisions.
 */
public class LexicalSequence implements Sequence<Token> {

    private final LexicalAnalyzer analyzer;

    private Token current;

    public LexicalSequence(LexicalAnalyzer analyzer) {
        this.analyzer = analyzer;
        current = analyzer.getNextToken().orElse(null);
    }

    @Override
    public Optional<Token> next() {
        Token next = current;
        current = analyzer.getNextToken().orElse(null);
        return Optional.ofNullable(next);
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Optional<Token> peek() {
        return Optional.ofNullable(current);
    }
}
