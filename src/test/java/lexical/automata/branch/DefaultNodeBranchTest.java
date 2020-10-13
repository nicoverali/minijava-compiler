package lexical.automata.branch;


import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.LexicalNode;
import lexical.automata.filter.LexicalFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
class DefaultNodeBranchTest {

    private static final Integer RESULT = 4;

    @Mock SourceCodeReader readerMock;

    @Mock LexicalException exceptionMock;

    @Mock LexicalFilter filterMock;
    @Mock LexicalNode<Integer> nextNodeMock;
    @InjectMocks DefaultNodeBranch<Integer> testSubject;

    @BeforeEach
    void setNodeMockAsNextNode(){
        testSubject.setNextNode(nextNodeMock);
    }

    @Test
    void delegate_nextCharacterDoesNotExist_doesNotDelegateToNextNode(){
        testSubject.delegate(readerMock);
        verify(nextNodeMock, never()).process(any());
    }

    @Test
    void delegate_nextCharacterDoesNotExist_returnsNull(){
        assertNull(testSubject.delegate(readerMock));
    }

    @Test
    void delegate_existsNextCharacterAndPassesFilter_consumesCharacterOnlyOnce(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(mock(CodeCharacter.class)));
        when(filterMock.test(anyChar())).thenReturn(true);

        testSubject.delegate(readerMock);
        verify(readerMock, times(1)).next();
    }

    @Test
    void delegate_existsNextCharacterAndPassesFilter_delegatesToNextNodeOnlyOnce(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(mock(CodeCharacter.class)));
        when(filterMock.test(anyChar())).thenReturn(true);

        testSubject.delegate(readerMock);
        verify(nextNodeMock, times(1)).process(readerMock);
    }

    @Test
    void delegate_existsNextCharacterAndPassesFilter_returnsDelegationResult(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(mock(CodeCharacter.class)));
        when(filterMock.test(anyChar())).thenReturn(true);
        when(nextNodeMock.process(readerMock)).thenReturn(RESULT);

        Integer result = testSubject.delegate(readerMock);
        assertEquals(RESULT, result);
    }

    @Test
    void delegate_existsNextCharacterAndPassesFilter_propagatesDelegationException(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(mock(CodeCharacter.class)));
        when(filterMock.test(anyChar())).thenReturn(true);
        when(nextNodeMock.process(readerMock)).thenThrow(exceptionMock);

        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.delegate(readerMock));
        assertEquals(exceptionMock, e);
    }

    @Test
    void delegate_existsNextCharacterButDoesNotPassFilter_doesNotDelegateToNextNode(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(mock(CodeCharacter.class)));

        testSubject.delegate(readerMock);
        verify(nextNodeMock, never()).process(readerMock);
    }

    @Test
    void delegate_existsNextCharacterButDoesNotPassFilter_returnsNull(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(mock(CodeCharacter.class)));

        assertNull(testSubject.delegate(readerMock));
    }

    @Test
    void delegate_existsNextCharacterButDoesNotPassFilter_doesNotConsumeCharacter(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(mock(CodeCharacter.class)));

        testSubject.delegate(readerMock);
        verify(readerMock, never()).next();
    }

}
