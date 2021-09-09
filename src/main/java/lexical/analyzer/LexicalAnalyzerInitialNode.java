package lexical.analyzer;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.Token;
import lexical.automata.AutomataToken;
import lexical.automata.LexicalNode;

import java.util.Optional;

import static lexical.TokenType.EOF;

/**
 * This class represents the initial node of an automata-based {@link LexicalAnalyzer}.
 * It takes a {@link SourceCodeReader} and provides clients with a method to retrieve all the {@link Token} of the file.
 * <br>
 * This initial node, will first try to skip all the unwanted characters, and then to generate a valid {@link Token}
 * with a provided {@link LexicalNode<AutomataToken>}.
 */
public class LexicalAnalyzerInitialNode {

    private final LexicalNode initialOmitterNode;
    private final LexicalNode initialTokenizerNode;
    private final SourceCodeReader reader;

    private boolean didReachEOF = false;

    /**
     * Creates a new initial node with the given {@link SourceCodeReader} and nodes.
     * The <code>initialOmitterNode</code> will be use to skip unwanted characters, and then the
     * <code>initialTokenizerNode</code> will be use to generate the next {@link Token}
     *
     * @param reader a {@link SourceCodeReader} to take its characters as input
     * @param initialOmitterNode a {@link LexicalNode} which skips unwanted characters
     * @param initialTokenizerNode a {@link LexicalNode} which generates {@link Token}
     */
    public LexicalAnalyzerInitialNode(SourceCodeReader reader, LexicalNode initialOmitterNode, LexicalNode initialTokenizerNode) {
        this.reader = reader;
        this.initialOmitterNode = initialOmitterNode;
        this.initialTokenizerNode = initialTokenizerNode;
    }

    /**
     * Returns the next {@link Token} emitted by this node {@link SourceCodeReader}.
     * <br>
     * Once the end of file is reached, following requests will be answered with an empty {@link Optional}.
     *
     * @return an {@link Optional} wrapping the next {@link Token} in the {@link SourceCodeReader}
     */
    public Optional<Token> getNextToken(){
        if (didReachEOF){
            return Optional.empty();
        }
        Token nextToken = process();
        didReachEOF = nextToken.getType() == EOF;
        return Optional.of(nextToken);
    }

    private Token process(){
        skipUnwantedCharacters();
        return initialTokenizerNode.process(reader);
    }

    private void skipUnwantedCharacters(){
        CodeCharacter currentCharacter;
        do {
            currentCharacter = reader.peek().orElse(null);
            initialOmitterNode.process(reader);
        } while (didOmitCharacters(reader, currentCharacter));
    }

    private boolean didOmitCharacters(SourceCodeReader reader, CodeCharacter previousCharacter){
        if (previousCharacter != null){
            return reader.peek()
                    .map(character -> !character.equals(previousCharacter))
                    .orElse(false);
        }
        return false;
    }

}
