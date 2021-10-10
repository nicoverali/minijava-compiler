package lexical;

import lexical.analyzer.LexicalAnalyzer;
import sequence.Sequence;

import java.util.Optional;

/**
 * This class is {@link Sequence} abstraction over the {@link Token} emitted by a {@link LexicalAnalyzer}.
 * This allows clients to {@link #peek()} tokens without consuming them, and thus take decisions.
 */
public class LexicalSequence implements Sequence<Token> {

    private final LexicalAnalyzer analyzer;

    private Token lastPeek;

    public LexicalSequence(LexicalAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public Optional<Token> next() {
        Token next;
        if (lastPeek != null){
            next = lastPeek;
            lastPeek = null;
        } else {
            next = analyzer.getNextToken().orElse(null);
        }
        return Optional.ofNullable(next);
    }

    @Override
    public boolean hasNext() {
        return lastPeek != null || (peek().isPresent());
    }

    @Override
    public Optional<Token> peek() {
        if (lastPeek != null){
            return Optional.of(lastPeek);
        }
        lastPeek = analyzer.getNextToken().orElse(null);
        return Optional.ofNullable(lastPeek);
    }
}
