package lexical.automata.tokenizer.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.Lexeme;
import lexical.Token;
import lexical.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcceptorStrategyTest {

    private static final int LINE_NUMBER = 1;
    private static final TokenType TYPE = TokenType.OP_PLUS;
    private static final String LEXEME = "ThisIsALexeme";
    private static final Lexeme TEST_LEXEME = new Lexeme(LINE_NUMBER, LEXEME);

    private AcceptorStrategy testSubject = new AcceptorStrategy(TYPE);

    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;


    @Test
    void onNoBranchMatch_shouldReturnTokenWithLexeme() {
        Optional<Token> returnedToken = testSubject.onNoBranchSelected(TEST_LEXEME, charMock);

        assertTrue(returnedToken.isPresent());
        assertEquals(TYPE, returnedToken.get().getType());
        assertEquals(LEXEME, returnedToken.get().getLexeme());
        assertEquals(LINE_NUMBER, returnedToken.get().getLineNumber());
    }

    @Test
    void onEndOfFile_nullLexeme_butCodeLine_shouldReturnTokenWithEmptyLexeme() {
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        Optional<Token> returnedToken = testSubject.onEndOfFile(null, lineMock);

        assertTrue(returnedToken.isPresent());
        assertEquals(TYPE, returnedToken.get().getType());
        assertEquals("", returnedToken.get().getLexeme());
        assertEquals(LINE_NUMBER, returnedToken.get().getLineNumber());
    }

    @Test
    void onEndOfFile_nullLexeme_nullCodeLine_shouldReturnTokenWithEmptyLexemeAtLine0() {
        Optional<Token> returnedToken = testSubject.onEndOfFile(null, null);

        assertTrue(returnedToken.isPresent());
        assertEquals(TYPE, returnedToken.get().getType());
        assertEquals("", returnedToken.get().getLexeme());
        assertEquals(0, returnedToken.get().getLineNumber());
    }

    @Test
    void onEndOfFile_withLexeme_shouldReturnTokenWithLexeme() {
        Optional<Token> returnedToken = testSubject.onEndOfFile(TEST_LEXEME, lineMock);

        assertTrue(returnedToken.isPresent());
        assertEquals(TYPE, returnedToken.get().getType());
        assertEquals(LEXEME, returnedToken.get().getLexeme());
        assertEquals(LINE_NUMBER, returnedToken.get().getLineNumber());
    }
}