package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.Token;
import lexical.automata.tokenizer.strategy.TokenizerNodeStrategy;
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

import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultTokenizerNodeTest {

    @Mock SourceCodeReader readerMock;
    @Mock CodeCharacter charMock;

    @Mock TokenizerBranch commonBranchMock;
    @Mock TryTokenizerBranch tryBranchMock;

    @Mock TokenizerNodeStrategy strategyMock;
    @InjectMocks DefaultTokenizerNode testSubject;

    @Test
    void tokenize_emptyFile_notConsumesNextCharacter(){
        when(readerMock.hasNext()).thenReturn(false);
        testSubject.tokenize(readerMock);

        verify(readerMock, never()).next();
    }

    @Test
    void tokenize_emptyFile_callOnEndOfFileWithNull(){
        when(readerMock.hasNext()).thenReturn(false);
        testSubject.tokenize(readerMock);

        verify(strategyMock).onEndOfFile(isNull());
    }

    @Test
    void tokenize_endOfFileWithAtTheEndOfLine_notConsumesNextCharacter(){
        when(readerMock.hasNext()).thenReturn(false);
        when(readerMock.getCurrentLine()).thenReturn(Optional.of(mock(CodeLine.class)));
        testSubject.tokenize(readerMock);

        verify(readerMock, never()).next();
    }

    @Test
    void tokenize_endOfFileWithAtTheEndOfLine_callOnEndOfFileWithLine(){
        CodeLine lineMock = mock(CodeLine.class);
        when(readerMock.hasNext()).thenReturn(false);
        when(readerMock.getCurrentLine()).thenReturn(Optional.of(lineMock));
        testSubject.tokenize(readerMock);

        verify(strategyMock).onEndOfFile(lineMock);
    }

    @Test
    void tokenize_noMatchingBranch_notConsumesNextCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        testSubject.tokenize(readerMock);

        verify(readerMock, never()).next();
    }

    @Test
    void tokenize_noMatchingBranch_callOnNoMatchingBranchWithNextCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        testSubject.tokenize(readerMock);
        verify(strategyMock).onNoBranchSelected(charMock);
    }

    @Test
    void tokenize_commonBranchMatches_notConsumesNextCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(commonBranchMock.getFilter()).thenReturn(value -> true);
        testSubject.addBranch(commonBranchMock);
        testSubject.tokenize(readerMock);

        verify(readerMock, never()).next();
    }

    @Test
    void tokenize_commonBranchMatches_delegates(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(commonBranchMock.getFilter()).thenReturn(value -> true);
        testSubject.addBranch(commonBranchMock);
        testSubject.tokenize(readerMock);

        verify(commonBranchMock).delegate(readerMock);
    }

    @Test
    void tokenize_tryBranchMatches_notConsumesNextCharacter(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(tryBranchMock.getFilter()).thenReturn(value -> true);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.tokenize(readerMock);

        verify(readerMock, never()).next();
    }

    @Test
    void tokenize_tryBranchMatches_delegates(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(tryBranchMock.getFilter()).thenReturn(value -> true);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.tokenize(readerMock);

        verify(tryBranchMock).tryDelegate(readerMock);
    }

    @Test
    void tokenize_neitherTryBranchNorCommonBranchMatch_callsOnNoBranchMatches(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(tryBranchMock.getFilter()).thenReturn(value -> false);
        when(commonBranchMock.getFilter()).thenReturn(value -> false);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addBranch(commonBranchMock);
        testSubject.tokenize(readerMock);

        verify(tryBranchMock, never()).tryDelegate(any());
        verify(commonBranchMock, never()).delegate(any());
        verify(strategyMock).onNoBranchSelected(charMock);
    }

    @Test
    void tokenize_tryBranchMatchesButFailsAndCommonBranchMatches_triesDelegateToTryBranchThenTriesDelegateCommonBranch(){
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(tryBranchMock.getFilter()).thenReturn(value -> true);
        when(tryBranchMock.tryDelegate(readerMock)).thenReturn(null);
        when(commonBranchMock.getFilter()).thenReturn(value -> true);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addBranch(commonBranchMock);
        testSubject.tokenize(readerMock);

        verify(tryBranchMock).tryDelegate(readerMock);
        verify(commonBranchMock).delegate(readerMock);
    }

    @Test
    void omit_allTryBranchesMatchButOnlyOneDelegatesSuccessfully_delegatesToSuccessfulTryBranch(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        TryTokenizerBranch successfulBranch = mock(TryTokenizerBranch.class);
        when(successfulBranch.getFilter()).thenReturn(value -> true);
        when(successfulBranch.tryDelegate(readerMock)).thenReturn(mock(Token.class));
        when(tryBranchMock.getFilter()).thenReturn(value -> true);
        when(tryBranchMock.tryDelegate(readerMock)).thenReturn(null);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(successfulBranch);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.tokenize(readerMock);

        verify(successfulBranch).tryDelegate(readerMock);
    }

}