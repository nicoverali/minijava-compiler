package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.LexicalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OmitterMiddleStrategyTest {

    private static final int LINE_SIZE = 10;
    private static final int COLUMN_NUMBER = 1;
    private static final String ERROR_MSG = "ThisIsAnErrorMsg";

    private OmitterMiddleStrategy testSubject = new OmitterMiddleStrategy(ERROR_MSG);

    @Mock CodeCharacter charMock;
    @Mock CodeLine lineMock;

    @Test
    void onNoBranchSelected_shouldThrowException() {
        when(charMock.getColumnNumber()).thenReturn(COLUMN_NUMBER);
        when(charMock.getCodeLine()).thenReturn(lineMock);
        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.onNoBranchSelected(charMock));

        assertEquals(ERROR_MSG, e.getMessage());
        assertEquals(COLUMN_NUMBER, e.getColumnNumber());
        assertEquals(lineMock, e.getExceptionLine());
    }

    @Test
    void onEndOfFile_emptyFile_shouldNotThrowException() {
        assertDoesNotThrow(() -> testSubject.onEndOfFile(null));
    }

    @Test
    void onEndOfFile_fileNotEmpty_shouldThrowException() {
        when(lineMock.getSize()).thenReturn(LINE_SIZE);
        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.onEndOfFile(lineMock));

        assertEquals(ERROR_MSG, e.getMessage());
        assertEquals(LINE_SIZE, e.getColumnNumber());
        assertEquals(lineMock, e.getExceptionLine());
    }
}