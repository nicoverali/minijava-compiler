package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.TokenType;
import lexical.automata.AutomataToken;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import util.RoyOsheroveTestNameGenerator;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TokenizeStrategyTest {

    private static final int LINE_SIZE = 10;
    private static final int LINE_NUMBER = 4;
    private static final int COLUMN_NUMBER = 4;
    private static final TokenType TYPE = TokenType.OP_PLUS;

    @Mock SourceCodeReader readerMock;

    @Mock CodeCharacter charMock;
    @Mock CodeLine lineMock;

    TokenizeStrategy testSubject = new TokenizeStrategy(TYPE);

    @Test
    void onNoBranchSelected_always_returnsTokenWithEmptyLexeme(){
        AutomataToken token = testSubject.onNoBranchSelected(readerMock, charMock);
        assertTrue(token.getLexeme().isEmpty());
    }

    @Test
    void onNoBranchSelected_always_returnsTokenWithGivenType(){
        AutomataToken token = testSubject.onNoBranchSelected(readerMock, charMock);
        assertEquals(TYPE, token.getType());
    }

    @Test
    void onNoBranchSelected_always_returnsTokenWithLexemeAtCharacterLineNumber(){
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(charMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);

        AutomataToken token = testSubject.onNoBranchSelected(readerMock, charMock);
        assertEquals(LINE_NUMBER, token.getLexeme().getLineNumber());
    }

    @Test
    void onNoBranchSelected_always_returnsTokenWithLexemeAtEndColumnNumber(){
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.getSize()).thenReturn(LINE_SIZE);

        AutomataToken token = testSubject.onNoBranchSelected(readerMock, charMock);
        assertEquals(LINE_SIZE, token.getLexeme().getColumnNumber());
    }

    @Test
    void onNoBranchSelected_always_returnsTokenAtCharacterLineNumber(){
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(charMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);

        AutomataToken token = testSubject.onNoBranchSelected(readerMock, charMock);
        assertEquals(LINE_NUMBER, token.getLineNumber());
    }

    @Test
    void onNoBranchSelected_always_returnsTokenAtCharacterEndColumnNumber(){
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.getSize()).thenReturn(LINE_SIZE);

        AutomataToken token = testSubject.onNoBranchSelected(readerMock, charMock);
        assertEquals(LINE_SIZE, token.getColumnNumber());
    }

    @Test
    void onNoBranchSelected_always_returnsTokenWithoutFirstLineAndFirstCharacter(){
        when(charMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(charMock.getColumnNumber()).thenReturn(COLUMN_NUMBER);

        AutomataToken token = testSubject.onNoBranchSelected(readerMock, charMock);
        assertFalse(token.getFirstLine().isPresent());
        assertFalse(token.getFirstCharacter().isPresent());
    }

    @Test
    void onEndOfFile_lineIsNull_returnsTokenWithEmptyLexeme(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, null);
        assertTrue(token.getLexeme().isEmpty());
    }

    @Test
    void onEndOfFile_lineIsNull_returnsTokenWithGivenType(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, null);
        assertEquals(TYPE, token.getType());
    }

    @Test
    void onEndOfFile_lineIsNull_returnsTokenWithLexemeAtLineAndColumn0(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, null);
        assertEquals(0, token.getLexeme().getLineNumber());
        assertEquals(0, token.getLexeme().getColumnNumber());
    }

    @Test
    void onEndOfFile_lineIsNull_returnsTokenAtLineAndColumn0(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, null);
        assertEquals(0, token.getLineNumber());
        assertEquals(0, token.getColumnNumber());
    }

    @Test
    void onEndOfFile_lineIsNull_returnsTokenWithoutFirstLineAndFirstCharacter(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, null);
        assertFalse(token.getFirstLine().isPresent());
        assertFalse(token.getFirstCharacter().isPresent());
    }

    @Test
    void onEndOfFile_lineIsNotNull_returnsTokenWithEmptyLexeme(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, lineMock);
        assertTrue(token.getLexeme().isEmpty());
    }

    @Test
    void onEndOfFile_lineIsNotNull_returnsTokenWithGivenType(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, lineMock);
        assertEquals(TYPE, token.getType());
    }

    @Test
    void onEndOfFile_lineIsNotNull_returnsTokenWithLexemeAtLineNumberAndEndColumnNumber(){
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(lineMock.getSize()).thenReturn(LINE_SIZE);
        AutomataToken token = testSubject.onEndOfFile(readerMock, lineMock);
        assertEquals(LINE_NUMBER, token.getLexeme().getLineNumber());
        assertEquals(LINE_SIZE, token.getLexeme().getColumnNumber());
    }

    @Test
    void onEndOfFile_lineIsNotNull_returnsTokenAtLineNumberAndEndColumnNumber(){
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(lineMock.getSize()).thenReturn(LINE_SIZE);
        AutomataToken token = testSubject.onEndOfFile(readerMock, lineMock);
        assertEquals(LINE_NUMBER, token.getLineNumber());
        assertEquals(LINE_SIZE, token.getColumnNumber());
    }

    @Test
    void onEndOfFile_lineIsNotNull_returnsTokenWithFirstLineButWithoutFirstCharacter(){
        AutomataToken token = testSubject.onEndOfFile(readerMock, lineMock);
        //noinspection OptionalGetWithoutIsPresent
        assertEquals(lineMock, token.getFirstLine().get());
        assertFalse(token.getFirstCharacter().isPresent());
    }

}