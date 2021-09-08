package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.NodeBranch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import util.RoyOsheroveTestNameGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultLexicalNodeTest {

    private static final Integer RESULT = 4;
    private static final String NAME = "This is a test node";

    @Mock SourceCodeReader readerMock;
    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;

    @Mock LexicalException exceptionMock;

    @Mock NodeBranch<Integer> branchMock;
    @Mock LexicalNodeStrategy<Integer> strategyMock;
    DefaultLexicalNode<Integer> testSubject = new DefaultLexicalNode<>(NAME);

    @BeforeEach
    void addMockedStrategy(){
        testSubject.setStrategy(strategyMock);
    }

    @Test
    void process_emptyFile_callOnEndOfFileWithoutLine(){
        testSubject.process(readerMock);
        verify(strategyMock).onEndOfFile(eq(readerMock), isNull());
    }

    @Test
    void process_emptyFile_returnStrategyResult(){
        when(strategyMock.onEndOfFile(readerMock, null)).thenReturn(RESULT);

        Integer result = testSubject.process(readerMock);
        assertEquals(RESULT, result);
    }

    @Test
    void process_emptyFile_propagateStrategyResult(){
        when(strategyMock.onEndOfFile(readerMock, null)).thenThrow(exceptionMock);

        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.process(readerMock));
        assertEquals(exceptionMock, e);
    }

    @Test
    void process_existsNextCharacterAndCanDelegate_doesNotConsumeCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.delegate(readerMock)).thenReturn(RESULT);

        testSubject.process(readerMock);
        verify(readerMock, never()).next();
    }

    @Test
    void process_existsNextCharacterAndCanDelegate_returnDelegationResult(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.delegate(readerMock)).thenReturn(RESULT);
        testSubject.addBranch(branchMock);

        Integer result = testSubject.process(readerMock);
        verify(branchMock).delegate(readerMock);
        assertEquals(RESULT, result);
    }

    @Test
    void process_existsNextCharacterAndCanDelegate_propagateDelegationException(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.delegate(readerMock)).thenThrow(exceptionMock);
        testSubject.addBranch(branchMock);

        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.process(readerMock));
        assertEquals(exceptionMock, e);
    }

    @Test
    void process_existsNextCharacterButCannotDelegate_doesNotConsumeCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.delegate(readerMock)).thenReturn(null);

        testSubject.process(readerMock);
        verify(readerMock, never()).next();
    }

    @Test
    void process_existsNextCharacterButCannotDelegate_returnStrategyResult(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.delegate(readerMock)).thenReturn(null);
        when(strategyMock.onNoBranchSelected(readerMock, charMock)).thenReturn(RESULT);

        Integer result = testSubject.process(readerMock);
        verify(strategyMock).onNoBranchSelected(readerMock, charMock);
        assertEquals(RESULT, result);
    }

    @Test
    void process_existsNextCharacterButCannotDelegate_propagateStrategyException(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.delegate(readerMock)).thenReturn(null);
        when(strategyMock.onNoBranchSelected(readerMock, charMock)).thenThrow(exceptionMock);

        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.process(readerMock));
        assertEquals(exceptionMock, e);
    }

    @Test
    void process_endOfFile_callOnEndOfFileWithCurrentLine(){
        when(readerMock.getCurrentLine()).thenReturn(Optional.of(lineMock));

        testSubject.process(readerMock);
        verify(strategyMock).onEndOfFile(readerMock, lineMock);
    }

    @Test
    void process_endOfFile_returnStrategyResult(){
        when(readerMock.getCurrentLine()).thenReturn(Optional.of(lineMock));
        when(strategyMock.onEndOfFile(eq(readerMock), any())).thenReturn(RESULT);

        Integer result = testSubject.process(readerMock);
        assertEquals(RESULT, result);
    }

    @Test
    void process_endOfFile_propagateStrategyResult(){
        when(readerMock.getCurrentLine()).thenReturn(Optional.of(lineMock));
        when(strategyMock.onEndOfFile(eq(readerMock), any())).thenThrow(exceptionMock);

        LexicalException e = assertThrows(LexicalException.class, () -> testSubject.process(readerMock));
        assertEquals(exceptionMock, e);
    }

    @Test
    void process_multipleBranches_delegateInOrder(){
        NodeBranch<Integer> firstBranch = mock(NodeBranch.class);
        NodeBranch<Integer> secondBranch = mock(NodeBranch.class);
        NodeBranch<Integer> thirdBranch = mock(NodeBranch.class);

        testSubject.addBranch(firstBranch);
        testSubject.addBranch(secondBranch);
        testSubject.addBranch(thirdBranch);

        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        testSubject.process(readerMock);

        InOrder inOrder = inOrder(firstBranch, secondBranch, thirdBranch);
        inOrder.verify(firstBranch).delegate(readerMock);
        inOrder.verify(secondBranch).delegate(readerMock);
        inOrder.verify(thirdBranch).delegate(readerMock);
    }

    @Test
    void process_multipleBranchesFirstDelegates_doesNotDelegateTheRest(){
        NodeBranch<Integer> firstBranch = mock(NodeBranch.class);
        NodeBranch<Integer> secondBranch = mock(NodeBranch.class);
        NodeBranch<Integer> thirdBranch = mock(NodeBranch.class);
        when(firstBranch.delegate(readerMock)).thenReturn(RESULT);

        testSubject.addBranch(firstBranch);
        testSubject.addBranch(secondBranch);
        testSubject.addBranch(thirdBranch);

        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        Integer result = testSubject.process(readerMock);

        assertEquals(RESULT, result);
        verify(firstBranch).delegate(readerMock);
        verify(secondBranch, never()).delegate(any());
        verify(thirdBranch, never()).delegate(any());
    }

    @Test
    void getName_nameSetInConstructor_returnName(){
        assertEquals(NAME, testSubject.getName());
    }

    @Test
    void getName_nameSetAfterCreation_returnName(){
        final String ANOTHER_NAME = "AnotherName";
        testSubject.setName(ANOTHER_NAME);
        assertEquals(ANOTHER_NAME, testSubject.getName());
    }

}