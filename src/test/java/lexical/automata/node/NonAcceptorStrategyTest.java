package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.Lexeme;
import lexical.automata.NodeBranch;
import lexical.automata.node.strategy.NonAcceptorStrategy;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import util.RoyOsheroveTestNameGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NonAcceptorStrategyTest {

    private static final String ERROR_MSG = "Error message";
    private static final String LINE_STRING = "This is a line of code";
    private static final int LINE_NUMBER = 4;
    private static final int COL_NUMBER = 8;

    @Mock SourceCodeReader readerMock;
    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;


    @Test
    void process_emptyFile_throwsException(){
        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
    }

    @Test
    void process_emptyFile_exceptionLineNumberIs0(){
        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
        assertEquals(0, exception.getLineNumber());
    }

    @Test
    void process_emptyFile_exceptionColumnNumberIs0(){
        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
        assertEquals(0, exception.getColumnNumber());
    }

    @Test
    void process_emptyFile_exceptionLineIsEmpty(){
        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
        assertEquals("", exception.getLine());
    }

    @Test
    void process_emptyFile_exceptionLexemeIsEmpty(){
        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
        assertEquals("", exception.getLexeme());
    }

    @Test
    void process_endOfFile_lineShouldBeLastLine(){
        when(readerMock.getLastLine()).thenReturn(Optional.of(lineMock));
        when(lineMock.toString()).thenReturn(LINE_STRING);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
        assertEquals(LINE_STRING, exception.getLine());
    }

    @Test
    void process_endOfFile_lineNumberShouldBeLastLine(){
        when(readerMock.getLastLine()).thenReturn(Optional.of(lineMock));
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
        assertEquals(LINE_NUMBER, exception.getLineNumber());
    }

    @Test
    void process_endOfFile_columnNumberShouldBeLastLineSize(){
        when(readerMock.getLastLine()).thenReturn(Optional.of(lineMock));
        when(lineMock.getSize()).thenReturn(COL_NUMBER);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onEndOfFile(readerMock, Lexeme.empty()));
        assertEquals(COL_NUMBER, exception.getColumnNumber());
    }

    @Test
    void process_noBranchSelected_shouldThrowException(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.toString()).thenReturn(LINE_STRING);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        assertThrows(LexicalException.class, () -> strategy.onNoBranchSelected(readerMock, Lexeme.empty(), charMock));
    }

    @Test
    void process_noBranchSelected_exceptionShouldContainCurrentLexeme(){
        Lexeme lexeme = mock(Lexeme.class);
        when(lexeme.toString()).thenReturn("Lexeme");
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.toString()).thenReturn(LINE_STRING);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onNoBranchSelected(readerMock, lexeme, charMock));
        assertEquals("Lexeme", exception.getLexeme());
    }

    @Test
    void process_noBranchSelected_exceptionLineShouldBeNextCharLine(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.toString()).thenReturn(LINE_STRING);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onNoBranchSelected(readerMock, Lexeme.empty(), charMock));
        assertEquals(LINE_STRING, exception.getLine());
    }

    @Test
    void process_noBranchSelected_exceptionLineNumberShouldBeNextCharLine(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(charMock.getLineNumber()).thenReturn(LINE_NUMBER);
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.toString()).thenReturn(LINE_STRING);
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onNoBranchSelected(readerMock, Lexeme.empty(), charMock));
        assertEquals(LINE_NUMBER, exception.getLineNumber());
    }

    @Test
    void process_noBranchSelected_exceptionColumnNumberShouldBeNextCharNumber(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(charMock.getCodeLine()).thenReturn(lineMock);
        when(lineMock.toString()).thenReturn(LINE_STRING);
        when(charMock.getColumnNumber()).thenReturn(COL_NUMBER);

        NonAcceptorStrategy strategy = new NonAcceptorStrategy(ERROR_MSG);
        LexicalException exception = assertThrows(LexicalException.class, () -> strategy.onNoBranchSelected(readerMock, Lexeme.empty(), charMock));
        assertEquals(COL_NUMBER, exception.getColumnNumber());
    }

}