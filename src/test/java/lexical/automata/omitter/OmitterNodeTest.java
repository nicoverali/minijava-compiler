package lexical.automata.omitter;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.reader.SourceCodeReader;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.omitter.strategy.OmitterNodeStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OmitterNodeTest {

    @Mock SourceCodeReader readerMock;
    @Mock CodeCharacter charMock;
    @Mock CodeLine lineMock;

    @Mock NodeBranchContainer<OmitterBranch> branchContainerMock;
    @Mock OmitterNode nextNodeMock;
    @Mock OmitterBranch branchMock;

    @Mock OmitterNodeStrategy strategyMock;

    @InjectMocks OmitterNode testSubject;

    @DisplayName("On empty file (no lines), should call strategy onEndOfFile with null current line")
    @Test
    void onEmptyFile_shouldCallOnEndOfFileWithNull(){
        when(readerMock.hasNext()).thenReturn(false);

        testSubject.omit(readerMock);

        verify(strategyMock).onEndOfFile(isNull());
    }

    @DisplayName("On EOF, should call strategy onEndOfFile with current line")
    @Test
    void onEmptyFile_shouldCallOnEndOfFileWithCurrentLine(){
        when(readerMock.hasNext()).thenReturn(false);
        when(readerMock.getCurrentLine()).thenReturn(Optional.of(lineMock));

        testSubject.omit(readerMock);

        verify(strategyMock).onEndOfFile(lineMock);
    }

    @DisplayName("On no matching branch, should not consume a character from reader")
    @Test
    void onNoMatchingBranch_shouldNotConsumeCharacter(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));

        testSubject.omit(readerMock);

        verify(readerMock, never()).getNext();
    }

    @DisplayName("On no matching branch, should call strategy onNoMatchingBranch with current character")
    @Test
    void onNoMatchingBranch_shouldCallOnNoMatchingBranchWithCurrentCharacter(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));

        testSubject.omit(readerMock);

        verify(strategyMock).onNoBranchSelected(charMock);
    }

    @DisplayName("On matching branch, should consume character from reader")
    @Test
    void onMatching_shouldConsumeCharacter(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(branchContainerMock.selectBranch(charMock)).thenReturn(Optional.of(branchMock));
        when(branchMock.getNextNode()).thenReturn(nextNodeMock);

        testSubject.omit(readerMock);

        verify(readerMock).getNext();
    }

    @DisplayName("On matching branch, should delegate")
    @Test
    void onMatching_shouldDelegate(){
        when(readerMock.hasNext()).thenReturn(true);
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(branchContainerMock.selectBranch(charMock)).thenReturn(Optional.of(branchMock));
        when(branchMock.getNextNode()).thenReturn(nextNodeMock);

        testSubject.omit(readerMock);

        verify(nextNodeMock).omit(readerMock);
    }



}