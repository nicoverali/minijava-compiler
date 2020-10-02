package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitialNodeStrategyTest {

    private static final String LINE = "This is a line";
    private static final int LINE_NUMBER = 1;
    private static final int COLUMN_NUMBER = 1;
    private static final String LEXEME = "ThisIsALexeme";
    private static final Lexeme TEST_LEXEME = new Lexeme(LINE_NUMBER, LEXEME);

    private InitialNodeStrategy testSubject = new InitialNodeStrategy();

    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;

    @Test
    void onNoBranchMatch_shouldThrowException() {
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(charMock.getColumnNumber()).thenReturn(COLUMN_NUMBER);
        when(lineMock.toString()).thenReturn(LINE);
        LexicalException exception = assertThrows(LexicalException.class,
                                                () -> testSubject.onNoBranchSelected(TEST_LEXEME, charMock));

        assertEquals(COLUMN_NUMBER, exception.getColumnNumber());
        assertEquals(LINE, exception.getLine());
    }

    @Test
    void onEndOfFile_shouldReturnEmptyOptional() {
        Optional<Token> returnedToken = testSubject.onEndOfFile(null, lineMock);
        assertFalse(returnedToken.isPresent());


    }
}