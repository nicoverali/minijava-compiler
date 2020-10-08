package lexical.automata.omitter;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.filter.LexicalFilter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import util.RoyOsheroveTestNameGenerator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OmitterBranchTest {

    @Mock SourceCodeReader readerMock;

    @Mock LexicalFilter filterMock;
    @Mock OmitterNode nodeMock;
    @InjectMocks OmitterBranch testSubject;

    @Test
    void delegate_delegationSuccessful_delegatesAndConsumesNextCharacter() {
        testSubject.delegate(readerMock);
        verify(nodeMock).omit(readerMock);
        verify(readerMock).next();
    }

    @Test
    void delegate_delegationException_consumesCharacterAndPropagatesException() {
        doThrow(mock(LexicalException.class)).when(nodeMock).omit(readerMock);

        assertThrows(LexicalException.class, () -> testSubject.delegate(readerMock));

        verify(readerMock).next();
        verifyNoMoreInteractions(readerMock);

    }
}