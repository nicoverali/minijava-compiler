package lexical.automata.helper;

import io.code.CodeCharacter;
import lexical.automata.NodeBranch;
import lexical.automata.filter.LexicalFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NodeBranchContainerTest {

    private final NodeBranchContainer<NodeBranch<?>> testSubject = new NodeBranchContainer<>();

    @Mock CodeCharacter charMock;
    @Mock NodeBranch<?> firstBranchMock;
    @Mock LexicalFilter firstFilterMock;
    @Mock NodeBranch<?> secondBranchMock;
    @Mock LexicalFilter secondFilterMock;


    @DisplayName("If container is empty, should return an empty optional")
    @Test
    void containerEmpty_shouldReturnEmptyOptional() {
        assertFalse(testSubject.getMatchingBranch(charMock).isPresent());
    }

    @DisplayName("With a single branch and matching character, should return the branch")
    @ParameterizedTest
    @ValueSource(chars = {'a', '\n', '\t', '"'})
    void singleBranch_matchingChar_shouldReturnBranch(char testCharacter){
        when(charMock.getValue()).thenReturn(testCharacter);
        when(firstBranchMock.getFilter()).thenReturn(firstFilterMock);
        when(firstFilterMock.test(testCharacter)).thenReturn(true);
        testSubject.addBranch(firstBranchMock);

        assertTrue(testSubject.getMatchingBranch(charMock).isPresent());
        assertEquals(firstBranchMock, testSubject.getMatchingBranch(charMock).get());
        verify(firstFilterMock, atLeastOnce()).test(testCharacter);
    }

    @DisplayName("With a single branch but no matching character, should return the branch")
    @ParameterizedTest
    @ValueSource(chars = {'a', '\n', '\t', '"'})
    void singleBranch_notMatchingChar_shouldReturnEmptyOptional(char testCharacter){
        when(charMock.getValue()).thenReturn(testCharacter);
        when(firstBranchMock.getFilter()).thenReturn(firstFilterMock);
        when(firstFilterMock.test(testCharacter)).thenReturn(false);
        testSubject.addBranch(firstBranchMock);

        assertFalse(testSubject.getMatchingBranch(charMock).isPresent());
        verify(firstFilterMock, atLeastOnce()).test(testCharacter);
    }

    @DisplayName("With two branches, both match, should return any of them")
    @ParameterizedTest
    @ValueSource(chars = {'a', '\n', '\t', '"'})
    void twoBranches_bothMatch_shouldReturnAny(char testCharacter){
        when(charMock.getValue()).thenReturn(testCharacter);
        when(firstBranchMock.getFilter()).thenReturn(firstFilterMock);
        when(firstFilterMock.test(testCharacter)).thenReturn(true);
        lenient().when(secondBranchMock.getFilter()).thenReturn(secondFilterMock);
        lenient().when(secondFilterMock.test(testCharacter)).thenReturn(true);
        testSubject.addBranch(firstBranchMock);
        testSubject.addBranch(secondBranchMock);

        Optional<NodeBranch<?>> selectedBranch = testSubject.getMatchingBranch(charMock);
        assertTrue(selectedBranch.isPresent());
        assertTrue(selectedBranch.get() == firstBranchMock || selectedBranch.get() == secondBranchMock);
    }

    @DisplayName("With two branches, neither match, should return empty optional")
    @ParameterizedTest
    @ValueSource(chars = {'a', '\n', '\t', '"'})
    void twoBranches_neitherMatch_shouldReturnEmptyOptional(char testCharacter){
        when(charMock.getValue()).thenReturn(testCharacter);
        when(firstBranchMock.getFilter()).thenReturn(firstFilterMock);
        when(firstFilterMock.test(testCharacter)).thenReturn(false);
        when(secondBranchMock.getFilter()).thenReturn(secondFilterMock);
        when(secondFilterMock.test(testCharacter)).thenReturn(false);
        testSubject.addBranch(firstBranchMock);
        testSubject.addBranch(secondBranchMock);

        Optional<NodeBranch<?>> selectedBranch = testSubject.getMatchingBranch(charMock);
        assertFalse(selectedBranch.isPresent());
    }

    @DisplayName("With two branches, only one matches, should return the matching branch")
    @ParameterizedTest
    @ValueSource(chars = {'a', '\n', '\t', '"'})
    void twoBranches_onlyOneMatches_shouldReturnMatchingBranch(char testCharacter){
        when(charMock.getValue()).thenReturn(testCharacter);
        when(firstBranchMock.getFilter()).thenReturn(firstFilterMock);
        when(firstFilterMock.test(testCharacter)).thenReturn(false);
        when(secondBranchMock.getFilter()).thenReturn(secondFilterMock);
        when(secondFilterMock.test(testCharacter)).thenReturn(true);
        testSubject.addBranch(firstBranchMock);
        testSubject.addBranch(secondBranchMock);

        Optional<NodeBranch<?>> selectedBranch = testSubject.getMatchingBranch(charMock);
        assertTrue(selectedBranch.isPresent());
        assertEquals(secondBranchMock, selectedBranch.get());

    }

}