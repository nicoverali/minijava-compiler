package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OmitterMiddleStrategyTest {

    private static final String LINE = "This is a line";
    private static final int LINE_NUMBER = 1;
    private static final int COLUMN_NUMBER = 1;
    private static final String ERROR_MSG = "ThisIsAnErrorMsg";

    private final OmitterMiddleStrategy testSubject = new OmitterMiddleStrategy(ERROR_MSG);

    @Mock CodeCharacter charMock;
    @Mock CodeLine lineMock;

    @DisplayName("If no branch is selected, then should throw a LexicalException with empty lexeme")
    @Test
    void onNoBranchSelected_shouldThrowException_withEmptyLexeme() {
        when(charMock.getColumnNumber()).thenReturn(COLUMN_NUMBER);
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.toString()).thenReturn(LINE);
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.onNoBranchSelected(charMock));

        assertEquals(ERROR_MSG, e.getMessage());
        assertEquals("", e.getLexeme().toString());
        assertEquals(LINE_NUMBER, e.getLineNumber());
        assertEquals(COLUMN_NUMBER, e.getColumnNumber());
        assertEquals(LINE, e.getLine());
    }

    @DisplayName("If reaches EOF, without current line, then should not do anything since file is empty")
    @Test
    void onEndOfFile_withoutLine_shouldThrowException() {
        assertDoesNotThrow(() -> testSubject.onEndOfFile(null));
    }

    @DisplayName("If reaches EOF, with a line, then should throw exception with empty lexeme")
    @Test
    void onEndOfFile_withLine_shouldThrowException() {
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(lineMock.toString()).thenReturn(LINE);
        when(lineMock.getSize()).thenReturn(LINE.length());
        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.onEndOfFile(lineMock));

        assertEquals(ERROR_MSG, e.getMessage());
        assertEquals("", e.getLexeme().toString());
        assertEquals(LINE_NUMBER, e.getLineNumber());
        assertEquals(LINE.length(), e.getColumnNumber());
        assertEquals(LINE, e.getLine());
    }
}