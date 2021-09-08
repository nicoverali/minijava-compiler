package lexical.automata.branch;

import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.NodeBranch;
import org.junit.jupiter.api.BeforeEach;
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
class TryNodeBranchTest {

    private static final int READ_AHEAD_LIMIT = 10;
    private static final Integer RESULT = 4;

    @Mock SourceCodeReader readerMock;

    @Mock NodeBranch<Integer> decoratedMock;
    TryNodeBranch<Integer> testSubject;

    @BeforeEach
    void createTestSubject(){
        testSubject = new TryNodeBranch<>(decoratedMock, READ_AHEAD_LIMIT);
    }

    @Test
    void delegate_decoratedReturnsValue_returnsDecoratedResult(){
        when(decoratedMock.delegate(readerMock)).thenReturn(RESULT);
        Integer result = testSubject.delegate(readerMock);
        assertEquals(RESULT, result);
    }

    @Test
    void delegate_decoratedReturnsValue_doesNotConsumeCharacter(){
        when(decoratedMock.delegate(readerMock)).thenReturn(RESULT);
        testSubject.delegate(readerMock);
        verify(readerMock, never()).next();
    }

    @Test
    void delegate_decoratedReturnsNull_returnsNull(){
        when(decoratedMock.delegate(readerMock)).thenReturn(null);
        assertNull(testSubject.delegate(readerMock));
    }

    @Test
    void delegate_decoratedReturnsNull_doesNotConsumeCharacter(){
        when(decoratedMock.delegate(readerMock)).thenReturn(null);
        testSubject.delegate(readerMock);
        verify(readerMock, never()).next();
    }

    @Test
    void delegate_decoratedThrowsException_returnsNull(){
        when(decoratedMock.delegate(readerMock)).thenThrow(mock(LexicalException.class));
        assertNull(testSubject.delegate(readerMock));
    }

    @Test
    void delegate_decoratedThrowsException_doesNotConsumeCharacter(){
        when(decoratedMock.delegate(readerMock)).thenThrow(mock(LexicalException.class));
        testSubject.delegate(readerMock);
        verify(readerMock, never()).next();
    }

    @Test
    void delegate_decoratedThrowsANonLexicalException_propagatesDecoratedException(){
        when(decoratedMock.delegate(readerMock)).thenThrow(mock(RuntimeException.class));
        assertThrows(Exception.class, () -> testSubject.delegate(readerMock));
    }

}