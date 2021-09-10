package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.Lexeme;
import lexical.automata.NodeBranch;
import lexical.automata.node.strategy.LexicalNodeStrategy;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BaseLexicalNodeTest {

    private static final String NAME = "This is a test node";

    @Mock
    Token RESULT;
    @Mock
    LexicalNodeStrategy strategyMock;
    @Mock
    SourceCodeReader readerMock;
    @Mock
    CodeCharacter charMock;

    @Mock
    LexicalException exceptionMock;

    @Mock
    NodeBranch branchMock;

    @Test
    void process_existsNextCharacterAndCanDelegate_consumesCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.test(any())).thenReturn(true);
        when(branchMock.delegate(eq(readerMock), any())).thenReturn(RESULT);

        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.addBranch(branchMock);
        node.process(readerMock, Lexeme.empty());
        verify(readerMock, times(1)).next();
    }

    @Test
    void process_existsNextCharacterAndCanDelegate_returnDelegationResult(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.test(any())).thenReturn(true);
        when(branchMock.delegate(eq(readerMock), any())).thenReturn(RESULT);

        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.addBranch(branchMock);
        Token result = node.process(readerMock, Lexeme.empty());

        verify(branchMock).test(charMock);
        verify(branchMock).delegate(eq(readerMock), any());
        assertEquals(RESULT, result);
    }

    @Test
    void process_existsNextCharacterAndCanDelegate_propagateDelegationException(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.test(any())).thenReturn(true);
        when(branchMock.delegate(eq(readerMock), any())).thenThrow(exceptionMock);

        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.addBranch(branchMock);

        LexicalException e = assertThrows(LexicalException.class, () -> node.process(readerMock, Lexeme.empty()));
        assertEquals(exceptionMock, e);
    }

    @Test
    void process_existsNextCharacterButCannotDelegate_doesNotConsumeCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(branchMock.delegate(eq(readerMock), any())).thenReturn(null);

        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.process(readerMock, Lexeme.empty());
        verify(readerMock, never()).next();
    }

    @Test
    void process_multipleBranches_testInOrder(){
        NodeBranch firstBranch = mock(NodeBranch.class);
        NodeBranch secondBranch = mock(NodeBranch.class);
        NodeBranch thirdBranch = mock(NodeBranch.class);

        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.addBranch(firstBranch);
        node.addBranch(secondBranch);
        node.addBranch(thirdBranch);

        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        node.process(readerMock, Lexeme.empty());

        InOrder inOrder = inOrder(firstBranch, secondBranch, thirdBranch);
        inOrder.verify(firstBranch).test(charMock);
        inOrder.verify(secondBranch).test(charMock);
        inOrder.verify(thirdBranch).test(charMock);
    }

    @Test
    void process_multipleBranchesFirstDelegates_doesNotDelegateTheRest(){
        NodeBranch firstBranch = mock(NodeBranch.class);
        NodeBranch secondBranch = mock(NodeBranch.class);
        NodeBranch thirdBranch = mock(NodeBranch.class);
        when(firstBranch.test(any())).thenReturn(true);
        when(firstBranch.delegate(eq(readerMock), any())).thenReturn(RESULT);

        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.addBranch(firstBranch);
        node.addBranch(secondBranch);
        node.addBranch(thirdBranch);

        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        Token result = node.process(readerMock, Lexeme.empty());

        assertEquals(RESULT, result);
        verify(firstBranch).delegate(eq(readerMock), any());
        verify(secondBranch, never()).test(any());
        verify(thirdBranch, never()).test(any());
        verify(secondBranch, never()).delegate(any(), any());
        verify(thirdBranch, never()).delegate(any(), any());
    }

    @Test
    void process_multipleBranchesLastDelegates_doesNotDelegateTheRest(){
        NodeBranch firstBranch = mock(NodeBranch.class);
        NodeBranch secondBranch = mock(NodeBranch.class);
        NodeBranch thirdBranch = mock(NodeBranch.class);
        when(thirdBranch.test(any())).thenReturn(true);
        when(thirdBranch.delegate(eq(readerMock), any())).thenReturn(RESULT);

        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.addBranch(firstBranch);
        node.addBranch(secondBranch);
        node.addBranch(thirdBranch);

        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        Token result = node.process(readerMock, Lexeme.empty());

        assertEquals(RESULT, result);
        verify(firstBranch).test(charMock);
        verify(secondBranch).test(charMock);
        verify(thirdBranch).test(charMock);
        verify(thirdBranch).delegate(eq(readerMock), any());

        verify(firstBranch, never()).delegate(any(), any());
        verify(secondBranch, never()).delegate(any(), any());
    }

    @Test
    void getName_nameSetInConstructor_returnName(){
        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        assertEquals(NAME, node.getName());
    }

    @Test
    void getName_nameSetAfterCreation_returnName(){
        final String ANOTHER_NAME = "AnotherName";
        BaseLexicalNode node = new BaseLexicalNode(NAME, strategyMock);
        node.setName(ANOTHER_NAME);
        assertEquals(ANOTHER_NAME, node.getName());
    }
    
}
