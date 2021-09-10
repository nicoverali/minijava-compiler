package lexical.automata.branch;


import io.code.CodeCharacter;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.Lexeme;
import lexical.automata.LexicalNode;
import lexical.automata.branch.filter.LexicalFilter;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultNodeBranchTest {

    @Mock Token RESULT;

    @Mock SourceCodeReader readerMock;

    @Mock LexicalException exceptionMock;

    @Mock CodeCharacter charMock;
    @Mock LexicalFilter filterMock;
    @Mock LexicalNode nextNodeMock;
    @InjectMocks DefaultNodeBranch testSubject;

    @BeforeEach
    void setNodeMockAsNextNode(){
        testSubject.setNextNode(nextNodeMock);
    }

    @Test
    void test_filterPass_returnsTrue(){
        when(filterMock.test(anyChar())).thenReturn(true);
        assertTrue(testSubject.test(charMock));
    }

    @Test
    void test_filterReject_returnsFalse(){
        when(filterMock.test(anyChar())).thenReturn(false);
        assertFalse(testSubject.test(charMock));
    }

    @Test
    void delegate_doesNotHaveNextNode_throwsException(){
        testSubject.setNextNode(null);
        assertThrows(IllegalStateException.class, () -> testSubject.delegate(readerMock, Lexeme.empty()));
    }

    @Test
    void delegate_hasNextNode_delegates(){
        testSubject.setNextNode(nextNodeMock);
        testSubject.delegate(readerMock, Lexeme.empty());
        verify(nextNodeMock).process(eq(readerMock), any());
    }

    @Test
    void delegate_hasNextNode_passCurrentLexeme(){
        Lexeme lexeme = Lexeme.empty();
        testSubject.setNextNode(nextNodeMock);
        testSubject.delegate(readerMock, lexeme);
        verify(nextNodeMock).process(readerMock, lexeme);
    }

    @Test
    void delegate_nextNodeReturnsResult_returnsDelegationResult(){
        when(nextNodeMock.process(any(), any())).thenReturn(RESULT);
        testSubject.setNextNode(nextNodeMock);
        Token result = testSubject.delegate(readerMock, Lexeme.empty());
        assertEquals(RESULT, result);
    }

    @Test
    void delegate_nextNodeThrowsException_propagatesException(){
        when(nextNodeMock.process(any(), any())).thenThrow(exceptionMock);
        testSubject.setNextNode(nextNodeMock);
        LexicalException exception = assertThrows(LexicalException.class, () -> testSubject.delegate(readerMock, Lexeme.empty()));
        assertEquals(exceptionMock, exception);
    }

}
