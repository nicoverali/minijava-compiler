package lexical.automata.tokenizer;

import io.code.CodeCharacter;
import io.code.reader.SourceCodeReader;
import lexical.Lexeme;
import lexical.automata.helper.NodeBranchContainer;
import lexical.automata.omitter.OmitterBranch;
import lexical.automata.omitter.OmitterNode;
import lexical.automata.tokenizer.strategy.TokenizerNodeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenizerNodeTest {

    int LINE_NUMBER = 0;
    String LEXEME = "TestLexeme";
    @Mock Lexeme testLexeme;

    @Mock SourceCodeReader readerMock;
    @Mock CodeCharacter charMock;

    @Mock OmitterNode omitterNodeMock;
    @Mock OmitterBranch omitterSelectedBranch;
    @Mock TokenizerNode tokenizerNodeMock;
    @Mock TokenizerBranch tokenizerSelectedBranch;

    @Mock TokenizerNodeStrategy strategyMock;
    @Mock NodeBranchContainer<OmitterBranch> omitterBranches;
    @Mock NodeBranchContainer<TokenizerBranch> tokenizerBranches;

    TokenizerNode testSubject;

    @BeforeEach
    void setupTestSubject(){
        testSubject = new TokenizerNode(strategyMock, tokenizerBranches, omitterBranches);
    }

    @DisplayName("On empty file, should not consume a character")
    @Test
    void onEmptyFile_shouldNotConsumeChar(){
        when(readerMock.hasNext()).thenReturn(false);

        testSubject.tokenize(readerMock, testLexeme);

        verify(readerMock, never()).getNext();
    }

    @DisplayName("On empty file and previous lexeme, should call strategy onEndOfFile with lexeme")
    @Test
    void onEmptyFile_shouldCallOnEndOfFileWithLexeme(){
        when(readerMock.hasNext()).thenReturn(false);

        testSubject.tokenize(readerMock, testLexeme);
        verify(strategyMock).onEndOfFile(eq(testLexeme), isNull());
    }

    @DisplayName("On no matching branch, should not consume next character")
    @Test
    void onNoMatchingBranch_shouldNotConsumeChar(){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        testSubject.tokenize(readerMock, testLexeme);

        verify(readerMock, never()).getNext();
    }

    @DisplayName("On no matching branch, with previous lexeme, should call strategy onNoMatchingBranch")
    @Test
    void onNoMatchingBranch_shouldCallOnNoMatchingBranch(){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);

        testSubject.tokenize(readerMock, testLexeme);
        verify(strategyMock).onNoBranchSelected(testLexeme, charMock);
    }

    @DisplayName("With an omitter matching branch, and previous lexeme, should consume character")
    @Test
    void onMatchingOmitterBranch_previousLexeme_shouldConsumeCharacter(){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(omitterBranches.selectBranch(charMock)).thenReturn(Optional.of(omitterSelectedBranch));
        when(omitterSelectedBranch.getNextNode()).thenReturn(omitterNodeMock);

        testSubject.tokenize(readerMock, testLexeme);
        verify(readerMock).getNext();
    }


    @DisplayName("With a tokenizer matching branch, and previous lexeme, should consume character")
    @Test
    void onMatchingTokenizerBranch_previousLexeme_shouldConsumeCharacter(){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(tokenizerBranches.selectBranch(charMock)).thenReturn(Optional.of(tokenizerSelectedBranch));
        when(tokenizerSelectedBranch.getNextNode()).thenReturn(tokenizerNodeMock);

        testSubject.tokenize(readerMock, testLexeme);
        verify(readerMock).getNext();
    }


    @DisplayName("With an omitter matching branch, and previous lexeme, should delegate")
    @Test
    void onMatchingOmitterBranch_previousLexeme_shouldDelegate(){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(omitterBranches.selectBranch(any())).thenReturn(Optional.of(omitterSelectedBranch));
        when(omitterSelectedBranch.getNextNode()).thenReturn(omitterNodeMock);

        testSubject.tokenize(readerMock, testLexeme);
        verify(omitterNodeMock).omit(readerMock);
    }


    @DisplayName("With a tokenizer matching branch, and previous lexeme, should delegate")
    @Test
    void onMatchingTokenizerBranch_previousLexeme_shouldDelegate(){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(tokenizerBranches.selectBranch(charMock)).thenReturn(Optional.of(tokenizerSelectedBranch));
        when(tokenizerSelectedBranch.getNextNode()).thenReturn(tokenizerNodeMock);

        testSubject.tokenize(readerMock, testLexeme);
        verify(tokenizerNodeMock).tokenize(readerMock, testLexeme);
    }

    @DisplayName("With a tokenizer matching branch, previous lexeme and set to store character, should append lexeme")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '$', ' ', '\n', '\t'})
    void onMatchingTokenizerBranch_previousLexeme_setToStore_shouldAppendLexeme(char testingChar){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.getNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(charMock.getValue()).thenReturn(testingChar);
        when(tokenizerBranches.selectBranch(charMock)).thenReturn(Optional.of(tokenizerSelectedBranch));
        when(tokenizerSelectedBranch.shouldStoreCharacter()).thenReturn(true);
        when(tokenizerSelectedBranch.getNextNode()).thenReturn(tokenizerNodeMock);

        testSubject.tokenize(readerMock, testLexeme);
        verify(testLexeme, times(1)).append(testingChar);
    }


    @DisplayName("With a tokenizer matching branch, previous lexeme and set to skip character, should leave lexeme unmodified")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '$', ' ', '\n', '\t'})
    void onMatchingTokenizerBranch_previousLexeme_setToSkip_shouldLeaveLexemeUnmodified(char testingChar){
        when(readerMock.peekNext()).thenReturn(Optional.of(charMock));
        when(readerMock.getNext()).thenReturn(Optional.of(charMock));
        when(readerMock.hasNext()).thenReturn(true);
        when(tokenizerBranches.selectBranch(charMock)).thenReturn(Optional.of(tokenizerSelectedBranch));
        when(tokenizerSelectedBranch.getNextNode()).thenReturn(tokenizerNodeMock);

        testSubject.tokenize(readerMock, testLexeme);
        verify(testLexeme, never()).append(testingChar);
    }

}