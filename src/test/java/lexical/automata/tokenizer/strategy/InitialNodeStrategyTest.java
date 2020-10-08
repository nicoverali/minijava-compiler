package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitialNodeStrategyTest {

    private static final String LINE = "This is a line";
    private static final int LINE_NUMBER = 1;
    private static final int COLUMN_NUMBER = 1;

    private final InitialNodeStrategy testSubject = new InitialNodeStrategy();

    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;

    @DisplayName("If no branch matches, should throw an exception with the current character")
    @ParameterizedTest
    @ValueSource(chars = {'a', '$', ' ', '\n', '\t'})
    void onNoBranchMatch_shouldThrowException(char testCharacter) {
        when(charMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(charMock.getValue()).thenReturn(testCharacter);
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(charMock.getColumnNumber()).thenReturn(COLUMN_NUMBER);
        when(lineMock.toString()).thenReturn(LINE);
        LexicalException exception = assertThrows(LexicalException.class, () -> testSubject.onNoBranchSelected(charMock));

        assertEquals(String.valueOf(testCharacter), exception.getLexeme().toString());
        assertEquals(COLUMN_NUMBER, exception.getColumnNumber());
        assertEquals(LINE_NUMBER, exception.getLineNumber());
        assertEquals(LINE, exception.getLine());
    }

    @DisplayName("If reaches EOF, without any line, should return an EOF token with empty lexeme at line 0")
    @Test
    void onEndOfFile_withoutLine_shouldReturnToken_withEmptyLexeme_atLineZero() {
        Token returnedToken = testSubject.onEndOfFile(null);

        assertNotNull(returnedToken);
        assertEquals(TokenType.EOF, returnedToken.getType());
        assertEquals("", returnedToken.getLexeme().toString());
        assertEquals(0, returnedToken.getLexeme().getLineNumber());
        assertEquals(0, returnedToken.getLineNumber());
    }

    @DisplayName("If reaches EOF, with a line, should return an EOF token with empty lexeme")
    @Test
    void onEndOfFile_withLine_shouldReturnToken_withEmptyLexeme() {
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        Token returnedToken = testSubject.onEndOfFile(lineMock);

        assertNotNull(returnedToken);
        assertEquals(TokenType.EOF, returnedToken.getType());
        assertEquals("", returnedToken.getLexeme().toString());
        assertEquals(LINE_NUMBER, returnedToken.getLexeme().getLineNumber());
        assertEquals(LINE_NUMBER, returnedToken.getLineNumber());
    }
}