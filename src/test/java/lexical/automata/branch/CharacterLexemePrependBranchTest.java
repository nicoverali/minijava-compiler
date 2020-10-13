package lexical.automata.branch;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.AutomataToken;
import lexical.automata.NodeBranch;
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
class CharacterLexemePrependBranchTest {

    @Mock SourceCodeReader readerMock;
    @Mock CodeCharacter charMock;

    @Mock AutomataToken tokenMock;
    @Mock NodeBranch<AutomataToken> decoratedMock;
    @InjectMocks CharacterLexemePrependBranch testSubject;

    @Test
    void delegate_decoratedReturnsToken_prependsTokenLexemeAndReturnsResult(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(decoratedMock.delegate(readerMock)).thenReturn(tokenMock);

        AutomataToken result = testSubject.delegate(readerMock);
        assertEquals(tokenMock, result);
        verify(tokenMock).prependLexeme(charMock);
    }

    @Test
    void delegate_decoratedReturnsToken_doesNotConsumeCharacter(){
        when(decoratedMock.delegate(readerMock)).thenReturn(tokenMock);
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
    void delegate_decoratedThrowsException_prependsExceptionLexemeAndPropagatesIt(){
        LexicalException exceptionMock = mock(LexicalException.class);
        when(decoratedMock.delegate(readerMock)).thenThrow(exceptionMock);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));

        LexicalException e = assertThrows(LexicalException.class, () ->testSubject.delegate(readerMock));
        verify(exceptionMock).prependLexeme(charMock);
        assertEquals(exceptionMock, e);
    }

    @Test
    void delegate_decoratedThrowsException_doesNotConsumeCharacter(){
        when(decoratedMock.delegate(readerMock)).thenThrow(mock(LexicalException.class));
        assertThrows(LexicalException.class, () -> testSubject.delegate(readerMock));
        verify(readerMock, never()).next();
    }

}