package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.LexicalNode;
import lexical.automata.NodeBranch;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.omitter.OmitterBranch;
import lexical.automata.tokenizer.strategy.TokenizerNodeStrategy;
import org.jetbrains.annotations.NotNull;

import java.io.UncheckedIOException;
import java.util.Optional;

public class TokenizerNode implements LexicalNode<TokenizerBranch> {

    private final NodeBranchContainer<TokenizerBranch> tokenizerBranches;
    private final NodeBranchContainer<OmitterBranch> omitterBranches;
    private final TokenizerNodeStrategy strategy;

    public TokenizerNode(@NotNull TokenizerNodeStrategy strategy,
                         @NotNull NodeBranchContainer<TokenizerBranch> tokenizerBranches,
                         @NotNull NodeBranchContainer<OmitterBranch> omitterBranches) {
        this.strategy = strategy;
        this.tokenizerBranches = tokenizerBranches;
        this.omitterBranches = omitterBranches;
    }

    /**
     * Receives a {@link SourceCodeReader} and tries to match the next characters with a token pattern.
     * <br>
     * If it can match the characters, then it returns the next {@link Token}; if it can't, which means that the
     * source code has a lexical error, it will throw a {@link LexicalException}.
     * <br><br>
     * If the {@link SourceCodeReader} has reached the end, then this node will return an empty {@link Optional}
     * <br><br>
     * To match the characters with a pattern, it will use its attached {@link NodeBranch}
     *
     * @see #addBranch(TokenizerBranch) 
     *
     * @param reader a {@link SourceCodeReader} that emits characters of a source file
     * @return an {@link Optional} wrapping the next {@link Token} emitted by the <code>reader</code>
     * @throws UncheckedIOException if an I/O error occurs
     * @throws LexicalException if the source code has a lexical error
     */
    Optional<Token> tokenize(SourceCodeReader reader) throws UncheckedIOException, LexicalException{
        Optional<Integer> lineNumber = reader.peekNext().map(CodeCharacter::getLineNumber);
        if (lineNumber.isPresent()){
            return this.tokenize(reader, Lexeme.empty(lineNumber.get()));
        }
        return strategy.onEndOfFile(null, reader.getCurrentLine().orElse(null));
    }

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
     * @see #addBranch(TokenizerBranch)
     *
     * @param reader a {@link SourceCodeReader} that emits characters of a source file
     * @param currentLexeme a part of the final {@link Lexeme} to which the rest is appended
     * @return an {@link Optional} wrapping the next {@link Token} emitted by the <code>reader</code>
     * @throws UncheckedIOException if an I/O error occurs
     * @throws LexicalException if the source code has a lexical error
     */
    Optional<Token> tokenize(SourceCodeReader reader, Lexeme currentLexeme) throws UncheckedIOException, LexicalException{
        if (!reader.hasNext()){
            return strategy.onEndOfFile(currentLexeme, reader.getCurrentLine().orElse(null));
        }

        tryToOmitCharacters(reader);
        Optional<TokenizerBranch> selectedBranch = reader.peekNext().flatMap(tokenizerBranches::selectBranch);
        if (selectedBranch.isPresent()){
            return delegate(reader, selectedBranch.get(), currentLexeme);
        }
        //noinspection OptionalGetWithoutIsPresent (Already tested with hasNext)
        return strategy.onNoBranchSelected(currentLexeme, reader.peekNext().get());
    }

    private void tryToOmitCharacters(SourceCodeReader reader) throws UncheckedIOException {
        reader.peekNext()
                .flatMap(omitterBranches::selectBranch)
                .map(OmitterBranch::getNextNode)
                .ifPresent(omitterNode -> {
                    reader.getNext(); // Consume character
                    omitterNode.omit(reader);
                });
    }

    private Optional<Token> delegate(SourceCodeReader reader, TokenizerBranch branch, Lexeme currentLexeme) throws UncheckedIOException{
        reader.getNext()
                .map(CodeCharacter::getValue)
                .filter(c -> branch.shouldStoreCharacter())
                .ifPresent(currentLexeme::append);

        return branch.getNextNode().tokenize(reader, currentLexeme);
    }

    @Override
    public void addBranch(TokenizerBranch branch) {
        tokenizerBranches.addBranch(branch);
    }

    public void addBranch(OmitterBranch omitterBranch){
        omitterBranches.addBranch(omitterBranch);
    }

}
