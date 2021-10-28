package lexical;

import io.code.CodeLine;
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

    private String lastLine; // This is just for debug

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
            Optional<Token> token = analyzer.getNextToken();
            lastLine = token.flatMap(Token::getFirstLine).map(CodeLine::toString).orElse("");
            next = token.orElse(null);
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
        Optional<Token> peek = analyzer.getNextToken();
        lastLine = peek.flatMap(Token::getFirstLine).map(CodeLine::toString).orElse("");
        lastPeek = peek.orElse(null);
        return peek;
    }

    @Override
    public String toString() {
        return lastLine;
    }
}
