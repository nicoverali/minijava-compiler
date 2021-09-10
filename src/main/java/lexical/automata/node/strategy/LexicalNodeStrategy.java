package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.AutomataToken;
import lexical.automata.Lexeme;

/**
 * The strategy of a {@link lexical.automata.node.BaseLexicalNode} determines what it should do when the processing of
 * characters cannot be delegated.
 */
public interface LexicalNodeStrategy {

    /**
     * Determines what to do in case no branch can match a certain character.
     * <br>
     * It will return an element of type <code>T</code> if it can generate one, or a <code>null</code> value
     * <br><br>
     * If a lexical error is detected then it will throw a {@link LexicalException}
     *
     *
     * @param reader the {@link SourceCodeReader} used by the node
     * @param currentLexeme the lexeme built by the automata up to this point
     * @param nextCharacter the next {@link CodeCharacter}
     * @return a nullable {@link Token}
     * @throws LexicalException if the node detects a lexical error
     */
    AutomataToken onNoBranchSelected(SourceCodeReader reader, Lexeme currentLexeme, CodeCharacter nextCharacter) throws LexicalException;

    /**
     * Similar to {@link #onNoBranchSelected}, but determines what to do when the end of file is reached.
     *
     *
     * @param reader the {@link SourceCodeReader} used by the node
     * @param currentLexeme the lexeme built by the automata up to this point
     * @return a nullable {@link Token}
     * @throws LexicalException if the node detects a lexical error
     */
    AutomataToken onEndOfFile(SourceCodeReader reader, Lexeme currentLexeme) throws LexicalException;

}
