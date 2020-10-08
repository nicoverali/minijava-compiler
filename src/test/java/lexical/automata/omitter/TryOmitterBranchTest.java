package lexical.automata.omitter;

import io.code.reader.SourceCodeReader;
import lexical.LexicalException;
import lexical.automata.filter.LexicalFilter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
class TryOmitterBranchTest {

    @Mock
    SourceCodeReader readerMock;

    @Mock LexicalFilter filterMock;
    @Mock OmitterNode nodeMock;
    @InjectMocks TryOmitterBranch testSubject;

    @Test
    void tryDelegate_successfulDelegation_delegatesAndConsumesCharacter() {
        testSubject.tryDelegate(readerMock);

        verify(readerMock).next();
        verify(readerMock, never()).reset();
    }

    @Test
    void tryDelegate_delegationException_delegatesAndNotConsumesCharacter() {
        doThrow(LexicalException.class).when(nodeMock).omit(readerMock);
        assertThrows(LexicalException.class, () -> testSubject.tryDelegate(readerMock));

        InOrder inOrder = inOrder(readerMock, nodeMock);
        inOrder.verify(readerMock).mark(any());
        inOrder.verify(nodeMock).omit(readerMock);
        inOrder.verify(readerMock).reset();
    }
}