package lexical.automata.omitter;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.automata.omitter.strategy.OmitterNodeStrategy;
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
class DefaultOmitterNodeTest {

    @Mock SourceCodeReader readerMock;
    @Mock CodeCharacter charMock;
    @Mock CodeLine lineMock;


    @Mock TryOmitterBranch tryBranchMock;
    @Mock OmitterBranch branchMock;

    @Mock OmitterNodeStrategy strategyMock;
    @InjectMocks DefaultOmitterNode testSubject;

    @Test
    void omit_emptyFile_notConsumesNextCharacter(){
        when(readerMock.hasNext()).thenReturn(false);
        testSubject.omit(readerMock);

        verify(readerMock, never()).next();
    }

    @Test
    void omit_emptyFile_callOnEndOfFileWithNull(){
        when(readerMock.hasNext()).thenReturn(false);
        testSubject.omit(readerMock);

        verify(strategyMock).onEndOfFile(isNull());
    }

    @Test
    void omit_endOfFileAtTheEndOfALine_callOnEndOfFileWithLine(){
        when(readerMock.hasNext()).thenReturn(false);
        when(readerMock.getCurrentLine()).thenReturn(Optional.of(lineMock));

        testSubject.omit(readerMock);

        verify(strategyMock).onEndOfFile(lineMock);
    }

    @Test
    void omit_noBranchMatches_notConsumesNextCharacter(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        testSubject.omit(readerMock);

        verify(readerMock, never()).next();
    }

    @Test
    void omit_noBranchMatches_callOnNoBranchSelectedWithNextCharacter(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        testSubject.omit(readerMock);

        verify(strategyMock).onNoBranchSelected(charMock);
    }

    @Test
    void omit_commonBranchMatches_delegates(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(branchMock.getFilter()).thenReturn(value -> true);
        testSubject.addBranch(branchMock);
        testSubject.omit(readerMock);

        verify(branchMock).delegate(readerMock);
    }

    @Test
    void omit_tryBranchMatches_delegates(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(tryBranchMock.getFilter()).thenReturn(value -> true);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.omit(readerMock);

        verify(tryBranchMock).tryDelegate(readerMock);
    }

    @Test
    void omit_tryBranchDontMatchesButCommonBranchDoes_delegatesToCommonBranch(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(tryBranchMock.getFilter()).thenReturn(value -> false);
        when(branchMock.getFilter()).thenReturn(value -> true);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addBranch(branchMock);
        testSubject.omit(readerMock);

        verify(tryBranchMock, never()).tryDelegate(readerMock);
        verify(branchMock).delegate(readerMock);
    }

    @Test
    void omit_neitherTryBranchNorCommonBranchMatch_callOnNoBranchMatches(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(tryBranchMock.getFilter()).thenReturn(value -> false);
        when(branchMock.getFilter()).thenReturn(value -> false);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addBranch(branchMock);
        testSubject.omit(readerMock);

        verify(tryBranchMock, never()).tryDelegate(readerMock);
        verify(branchMock, never()).delegate(readerMock);
        verify(strategyMock).onNoBranchSelected(charMock);
    }

    @Test
    void omit_tryBranchMatchesButFailsAndCommonBranchMatches_triesDelegateToFirstBranchThenDelegateToSecond(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        when(tryBranchMock.getFilter()).thenReturn(value -> true);
        when(tryBranchMock.tryDelegate(readerMock)).thenReturn(false);
        when(branchMock.getFilter()).thenReturn(value -> true);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addBranch(branchMock);
        testSubject.omit(readerMock);

        verify(tryBranchMock).tryDelegate(readerMock);
        verify(branchMock).delegate(readerMock);
    }

    @Test
    void omit_allTryBranchesMatchButOnlyOneDelegatesSuccessfully_delegatesToSuccessfulTryBranch(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peek()).thenReturn(Optional.of(charMock));
        TryOmitterBranch successfulBranch = mock(TryOmitterBranch.class);
        when(successfulBranch.getFilter()).thenReturn(value -> true);
        when(successfulBranch.tryDelegate(readerMock)).thenReturn(true);
        when(tryBranchMock.getFilter()).thenReturn(value -> true);
        when(tryBranchMock.tryDelegate(readerMock)).thenReturn(false);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(successfulBranch);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.addTryBranch(tryBranchMock);
        testSubject.omit(readerMock);

        verify(successfulBranch).tryDelegate(readerMock);
    }


}