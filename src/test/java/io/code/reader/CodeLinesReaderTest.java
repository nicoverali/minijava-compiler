package io.code.reader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

interface CodeLinesReaderTest {

    String FIRST_LINE = "First line";
    String SECOND_LINE = "Second line";
    String THIRD_LINE = "Third line";
    String BLANK_LINE = "";

    CodeLinesReader createCodeLinesReader(String... lines);

    @DisplayName("Empty file, should return empty optional on get")
    @Test
    default void emptyFile_shouldReturnEmptyOptional_onGet(){
        CodeLinesReader testSubject = createCodeLinesReader();
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("Empty file, should return empty optional on peek")
    @Test
    default void emptyFile_shouldReturnEmptyOptional_onPeek(){
        CodeLinesReader testSubject = createCodeLinesReader();
        assertFalse(testSubject.peek().isPresent());
    }

    @DisplayName("Empty file, should return false on has next")
    @Test
    default void emptyFile_shouldReturnFalse_onHasNext(){
        CodeLinesReader testSubject = createCodeLinesReader();
        assertFalse(testSubject.hasNext());
    }

    @DisplayName("Single character, should return line without new line on get")
    @ParameterizedTest
    @ValueSource(chars = {'a', '$', '\t', ' '})
    default void singleCharacter_shouldReturnLineWithoutNewLine_onGet(char testCharacter){
        CodeLinesReader testSubject = createCodeLinesReader(String.valueOf(testCharacter));
        assertEquals(String.valueOf(testCharacter), testSubject.next().get().toString());
    }

    @DisplayName("Single character, should return line without new line on peek")
    @ParameterizedTest
    @ValueSource(chars = {'a', '$', '\t', ' '})
    default void singleCharacter_shouldReturnLineWithoutNewLine_onPeek(char testCharacter){
        CodeLinesReader testSubject = createCodeLinesReader(String.valueOf(testCharacter));
        assertEquals(String.valueOf(testCharacter), testSubject.peek().get().toString());
    }

    @DisplayName("Single character, should return true on has next")
    @ParameterizedTest
    @ValueSource(chars = {'a', '$', '\t', ' '})
    default void singleCharacter_shouldReturnTrue_onHasNext(char testCharacter){
        CodeLinesReader testSubject = createCodeLinesReader(String.valueOf(testCharacter));
        assertTrue(testSubject.hasNext());
    }

    @DisplayName("Single line, after getting first line, should return empty optional on get")
    @Test
    default void singleLine_afterGettingFirstLine_shouldReturnEmptyOptional_onGet(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE);
        assertEquals(FIRST_LINE, testSubject.next().get().toString());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("Single line, after getting first line, should return empty optional on peek")
    @Test
    default void singleLine_afterGettingFirstLine_shouldReturnEmptyOptional_onPeek(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE);
        assertEquals(FIRST_LINE, testSubject.next().get().toString());
        assertFalse(testSubject.peek().isPresent());
    }

    @DisplayName("Single line, after getting first line, should return false on has next")
    @Test
    default void singleLine_afterGettingFirstLine_shouldReturnFalse_onHasNext(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE);
        assertEquals(FIRST_LINE, testSubject.next().get().toString());
        assertFalse(testSubject.hasNext());
    }

    @DisplayName("Two lines, should return first line with new line character on get")
    @Test
    default void twoLines_shouldReturnFirstLineWithNewLine_onGet(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, SECOND_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
    }

    @DisplayName("Two lines, should return first line with new line character on peek")
    @Test
    default void twoLines_shouldReturnFirstLineWithNewLine_onPeek(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, SECOND_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.peek().get().toString());
    }

    @DisplayName("Two lines, after getting first line, should return second line without new line character on get")
    @Test
    default void twoLines_afterGettingFirstLine_shouldReturnSecondLineWithoutNewLine_onGet(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, SECOND_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
        assertEquals(SECOND_LINE, testSubject.next().get().toString());
    }

    @DisplayName("Two lines, after getting first line, should return second line without new line character on peek")
    @Test
    default void twoLines_afterGettingFirstLine_shouldReturnSecondLineWithoutNewLine_onPeek(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, SECOND_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
        assertEquals(SECOND_LINE, testSubject.peek().get().toString());
    }

    @DisplayName("Two lines, after getting first line, should return true on has next")
    @Test
    default void twoLines_afterGettingFirstLine_shouldReturnTrue_onHasNext(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, SECOND_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
        assertTrue(testSubject.hasNext());
    }

    @DisplayName("Three lines, second line blank, after getting first, should return second line with new line character on get")
    @Test
    default void threeLines_secondBlank_afterGettingFirst_shouldReturnSecondLineWithNewLine_onGet(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, BLANK_LINE, THIRD_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
        assertEquals("\n", testSubject.next().get().toString());
    }

    @DisplayName("Three lines, second line blank, after getting first, should return second line with new line character on peek")
    @Test
    default void threeLines_secondBlank_afterGettingFirst_shouldReturnSecondLineWithNewLine_onPeek(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, BLANK_LINE, THIRD_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
        assertEquals("\n", testSubject.peek().get().toString());
    }

    @DisplayName("Three lines, all blank, should return the first with new line, second without, and third should be ignored")
    @Test
    default void threeLines_allBlank_shouldReturnFirstWithNewLine_shouldReturnSecondWithoutNewLine_shouldIgnoreThird(){
        CodeLinesReader testSubject = createCodeLinesReader(BLANK_LINE, BLANK_LINE, BLANK_LINE);
        assertEquals("\n", testSubject.next().get().toString());
        assertEquals("", testSubject.next().get().toString());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("Three lines, first two blank, should return the first two with new line, and third without new line")
    @Test
    default void threeLines_firstTwoBlank_shouldReturnFirstTwoWithNewLine_shouldReturnThirdWithoutNewLine(){
        CodeLinesReader testSubject = createCodeLinesReader(BLANK_LINE, BLANK_LINE, THIRD_LINE);
        assertEquals("\n", testSubject.next().get().toString());
        assertEquals("\n", testSubject.next().get().toString());
        assertEquals(THIRD_LINE, testSubject.next().get().toString());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("Single line, peeking should not affect get")
    @Test
    default void singleLine_peekingShouldNotAffectGet(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE);
        assertEquals(FIRST_LINE, testSubject.peek().get().toString());
        assertEquals(FIRST_LINE, testSubject.next().get().toString());
        assertFalse(testSubject.peek().isPresent());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("Two lines, peeking should not affect get")
    @Test
    default void twoLines_peekingShouldNotAffectGet(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, SECOND_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.peek().get().toString());
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
        assertEquals(SECOND_LINE, testSubject.peek().get().toString());
        assertEquals(SECOND_LINE, testSubject.next().get().toString());
        assertFalse(testSubject.peek().isPresent());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("Three lines, peeking should not affect get")
    @Test
    default void threeLines_peekingShouldNotAffectGet(){
        CodeLinesReader testSubject = createCodeLinesReader(FIRST_LINE, SECOND_LINE, THIRD_LINE);
        assertEquals(FIRST_LINE+'\n', testSubject.peek().get().toString());
        assertEquals(FIRST_LINE+'\n', testSubject.next().get().toString());
        assertEquals(SECOND_LINE+'\n', testSubject.peek().get().toString());
        assertEquals(SECOND_LINE+'\n', testSubject.next().get().toString());
        assertEquals(THIRD_LINE, testSubject.peek().get().toString());
        assertEquals(THIRD_LINE, testSubject.next().get().toString());
        assertFalse(testSubject.peek().isPresent());
        assertFalse(testSubject.next().isPresent());
    }

}