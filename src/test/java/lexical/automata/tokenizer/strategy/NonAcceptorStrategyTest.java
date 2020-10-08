package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
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
class NonAcceptorStrategyTest {

    private static final String LINE = "This is a line";
    private static final int LINE_NUMBER = 1;
    private static final int COLUMN_NUMBER = 1;
    private static final String ERROR_MSG = "This is an error message";

    private final NonAcceptorStrategy testSubject = new NonAcceptorStrategy(ERROR_MSG);

    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;

    @DisplayName("If no branch matches, then should throw LexicalException with current character as lexeme")
    @ParameterizedTest
    @ValueSource(chars = {'a', '$', ' ', '\n', '\t'})
    void onNoBranchMatch_shouldThrowException(char testCharacter) {
        when(charMock.getValue()).thenReturn(testCharacter);
        when(charMock.getColumnNumber()).thenReturn(COLUMN_NUMBER);
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.toString()).thenReturn(LINE);
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);

        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.onNoBranchSelected(charMock));

        assertEquals(ERROR_MSG, e.getMessage());
        assertEquals(String.valueOf(testCharacter), e.getLexeme().toString());
        assertEquals(LINE_NUMBER, e.getLineNumber());
        assertEquals(COLUMN_NUMBER, e.getColumnNumber());
        assertEquals(LINE, e.getLine());
    }

    @DisplayName("If reached EOF without any line, should throw LexicalException with empty lexeme at line 0")
    @Test
    void onEndOfFile_withoutLine_shouldThrowException_withEmptyLexemeAtLineZero() {
        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.onEndOfFile(null));

        assertEquals(ERROR_MSG, e.getMessage());
        assertEquals("", e.getLexeme().toString());
        assertEquals(0, e.getLineNumber());
        assertEquals(0, e.getColumnNumber());
        assertEquals("", e.getLine());
    }

    @DisplayName("If reached EOF with a line, should accept and return a Token with empty lexeme")
    @Test
    void onEndOfFile_withLine_shouldThrowException_withEmptyLexeme() {
        when(lineMock.toString()).thenReturn(LINE);
        when(lineMock.getSize()).thenReturn(LINE.length());
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.onEndOfFile(lineMock));

        assertEquals(ERROR_MSG, e.getMessage());
        assertEquals("", e.getLexeme().toString());
        assertEquals(LINE_NUMBER, e.getLineNumber());
        assertEquals(LINE.length(), e.getColumnNumber());
        assertEquals(LINE, e.getLine());

    }
}