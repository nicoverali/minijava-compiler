package lexical.automata.tokenizer;

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
import sequence.MarkableAppendableSequenceTest;
import util.RoyOsheroveTestNameGenerator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TryTokenizerBranchTest {

    @Mock SourceCodeReader readerMock;

    @Mock LexicalFilter filterMock;
    @Mock TokenizerNode nodeMock;
    @InjectMocks TokenizerBranch testSubject;

    @Test
    void tryDelegate_successfullyDelegates_delegatesAndConsumesCharacter() {
        testSubject.delegate(readerMock);

        verify(readerMock).next();
        verify(readerMock, never()).reset();
        verify(nodeMock).tokenize(readerMock);
    }


    @Test
    void tryDelegate_delegationException_delegatesAndNotConsumesCharacter() {
        doThrow(LexicalException.class).when(nodeMock).tokenize(readerMock);
        testSubject.delegate(readerMock);

        InOrder inOrder = inOrder(readerMock, nodeMock);
        inOrder.verify(readerMock).mark(anyByte());
        inOrder.verify(nodeMock).tokenize(readerMock);
        inOrder.verify(readerMock).reset();
    }
}