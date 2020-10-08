package lexical.automata.tokenizer;

import io.code.reader.SourceCodeReader;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import org.jetbrains.annotations.NotNull;

import java.io.UncheckedIOException;
import java.util.Optional;

public interface TokenizerNode extends LexicalNode<TokenizerBranch, TryTokenizerBranch> {
    /**
     * Receives a {@link SourceCodeReader} and tries to match the next characters with a token pattern.
     * <br>
     * If it can match the characters, then it returns the next {@link Token}; if it can't, which means that the
     * source code has a lexical error, it will throw a {@link LexicalException}.
     * <br><br>
     * If the {@link SourceCodeReader} has reached the end, then this node will return an empty {@link Optional}
     * <br><br>
     * To match the characters with a pattern, it will use its attached {@link NodeBranch}
     * <br><br>
     * If a part of the final {@link Lexeme} is given, then this node will append the rest to that initial lexeme
     *
     * @see #addBranch
     * @see #addTryBranch
     *
     * @param reader a {@link SourceCodeReader} that emits characters of a source file
     * @return an {@link Optional} wrapping the next {@link Token} emitted by the <code>reader</code>
     * @throws UncheckedIOException if an I/O error occurs
     * @throws LexicalException if the source code has a lexical error
     */
    @NotNull
    Token tokenize(@NotNull SourceCodeReader reader) throws UncheckedIOException, LexicalException;
}
