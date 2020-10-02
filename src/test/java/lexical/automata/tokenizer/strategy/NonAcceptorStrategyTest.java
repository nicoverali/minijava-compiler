package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NonAcceptorStrategyTest {

    private static final int LINE_SIZE = 9;
    private static final int LINE_NUMBER = 1;
    private static final int COLUMN_NUMBER = 1;
    private static final String ERROR_MSG = "This is an error message";
    private static final String LEXEME = "ThisIsALexeme";
    private static final Lexeme TEST_LEXEME = new Lexeme(LINE_NUMBER, LEXEME);

    private NonAcceptorStrategy testSubject = new NonAcceptorStrategy(ERROR_MSG);

    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;

    @Test
    void onNoBranchMatch_shouldThrowException() {
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(charMock.getColumnNumber()).thenReturn(COLUMN_NUMBER);
        LexicalException exception = assertThrows(LexicalException.class,
                                                () -> testSubject.onNoBranchSelected(TEST_LEXEME, charMock));

        assertEquals(ERROR_MSG, exception.getMessage());
        assertEquals(COLUMN_NUMBER, exception.getColumnNumber());
        assertEquals(lineMock, exception.getExceptionLine());

    }

    @Test
    void onEndOfFile_shouldThrowException() {
        when(lineMock.getSize()).thenReturn(LINE_SIZE);
        LexicalException exception = assertThrows(LexicalException.class,
                                                () -> testSubject.onEndOfFile(null, lineMock));

        assertEquals(ERROR_MSG, exception.getMessage());
        assertEquals(LINE_SIZE, exception.getColumnNumber());
        assertEquals(lineMock, exception.getExceptionLine());

    }
}