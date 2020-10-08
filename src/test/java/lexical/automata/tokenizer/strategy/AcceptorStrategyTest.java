package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptorStrategyTest {

    private static final int LINE_NUMBER = 1;
    private static final TokenType TYPE = TokenType.OP_PLUS;

    private final AcceptorStrategy testSubject = new AcceptorStrategy(TYPE);

    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;


    @DisplayName("If no branch matches, then should accept and return a Token with empty lexeme")
    @ParameterizedTest
    @ValueSource(chars = {'a', '$', ' ', '\n', '\t'})
    void onNoBranchMatch_shouldReturnTokenWithLexeme(char testCharacter) {
        when(charMock.getLineNumber()).thenReturn(LINE_NUMBER);
        Token returnedToken = testSubject.onNoBranchSelected(charMock);

        assertNotNull(returnedToken);
        assertEquals(TYPE, returnedToken.getType());
        assertEquals(LINE_NUMBER, returnedToken.getLineNumber());
        assertEquals("", returnedToken.getLexeme().toString());
    }

    @DisplayName("If reached EOF without any line, should accept and return a Token with empty lexeme at line 0")
    @Test
    void onEndOfFile_withoutLine_shouldAccept_shouldReturnTokenWithEmptyLexemeAtLineZero() {
        Token returnedToken = testSubject.onEndOfFile(null);

        assertNotNull(returnedToken);
        assertEquals(TYPE, returnedToken.getType());
        assertEquals(0, returnedToken.getLineNumber());
        assertEquals("", returnedToken.getLexeme().toString());

    }

    @DisplayName("If reached EOF with a line, should accept and return a Token with empty lexeme")
    @Test
    void onEndOfFile_withLine_shouldAccept_shouldReturnTokenWithEmptyLexeme() {
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        Token returnedToken = testSubject.onEndOfFile(lineMock);

        assertNotNull(returnedToken);
        assertEquals(TYPE, returnedToken.getType());
        assertEquals(LINE_NUMBER, returnedToken.getLineNumber());
        assertEquals("", returnedToken.getLexeme().toString());

    }
}