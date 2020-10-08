package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.Lexeme;
import lexical.LexicalException;
import lexical.Token;
import lexical.automata.filter.LexicalFilter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import util.RoyOsheroveTestNameGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TokenizerBranchTest {

    @Mock SourceCodeReader readerMock;
    @Mock CodeCharacter charMock;
    @Mock LexicalException exceptionMock;
    @Mock Token tokenMock;
    @Mock Lexeme lexemeMock;

    @Mock LexicalFilter filterMock;
    @Mock TokenizerNode nodeMock;
    @Mock boolean shouldStoreCharacter;
    @InjectMocks TokenizerBranch testSubject;

    @Test
    void delegate_successfullyDelegates_delegatesAndConsumesCharacter() {
        when(nodeMock.tokenize(readerMock)).thenReturn(tokenMock);
        testSubject.delegate(readerMock);

        verify(readerMock).next();
        verifyNoMoreInteractions(readerMock);
        verify(nodeMock).tokenize(readerMock);
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', '$', ' ', '\n', '\t'})
    void delegate_successfullyDelegates_prependsCharacterToFinalToken(char testCharacter){
        when(charMock.getValue()).thenReturn(testCharacter);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(nodeMock.tokenize(readerMock)).thenReturn(tokenMock);
        when(tokenMock.getLexeme()).thenReturn(lexemeMock);
        testSubject.delegate(readerMock);

        verify(lexemeMock).prepend(testCharacter);
    }


    @Test
    void delegate_delegationException_consumesCharacterAndPropagatesException() {
        doThrow(LexicalException.class).when(nodeMock).tokenize(readerMock);
        assertThrows(LexicalException.class, () ->testSubject.delegate(readerMock));

        verify(readerMock).next();
        verifyNoMoreInteractions(readerMock);
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', '$', ' ', '\n', '\t'})
    void delegate_delegatesException_prependsCharacterToExceptionLexeme(char testCharacter){
        when(charMock.getValue()).thenReturn(testCharacter);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.next()).thenReturn(Optional.of(charMock));
        when(exceptionMock.getLexeme()).thenReturn(lexemeMock);
        doThrow(exceptionMock).when(nodeMock).tokenize(readerMock);
        testSubject.delegate(readerMock);

        verify(lexemeMock).prepend(testCharacter);
    }
}
