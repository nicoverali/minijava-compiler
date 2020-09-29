package io.code.reader;

import io.code.CodeCharacter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public interface SourceCodeReaderTest<T extends SourceCodeReader> {

    T createSourceCodeReader(String... lines) throws IOException;

    @DisplayName("With empty file, should return empty optional.")
    @Test
    default void onEmptyFile_shouldReturnEmptyCharacter() throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.getNext().isPresent());
    }

    @DisplayName("With empty file, repeat request, should always return empty optional.")
    @Test
    default void onEmptyFile_repeatRequest_shouldReturnEmptyCharacter() throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.getNext().isPresent());
        assertFalse(testSubject.getNext().isPresent());
        assertFalse(testSubject.getNext().isPresent());
        assertFalse(testSubject.getNext().isPresent());
    }

    @DisplayName("With empty file, peeking should return empty optional.")
    @Test
    default void onEmptyFile_peekShouldReturnEmptyCharacter() throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.peekNext().isPresent());
    }

    @DisplayName("With empty file, should not have current line.")
    @Test
    default void onEmptyFile_shouldNotHaveCurrentLine() throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.getCurrentLine().isPresent());
    }

    @DisplayName("With empty file, should not have a next character.")
    @Test
    default void onEmptyFile_shouldNotHaveNext() throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.hasNext());
    }

    @DisplayName("With single character, should return character and then empty.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_shouldReturnEmptyCharacter(char testCharacter) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        // Verify first character
        Optional<CodeCharacter> character = testSubject.getNext();
        assertTrue(character.isPresent());
        assertEquals(testCharacter, character.get().getValue());

        // Verify end of file
        assertFalse(testSubject.getNext().isPresent());

    }

    @DisplayName("With single character, should peek first character.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_shouldPeekFirstCharacter(char testCharacter) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        Optional<CodeCharacter> character = testSubject.peekNext();
        assertTrue(character.isPresent());
        assertEquals(testCharacter, character.get().getValue());

    }

    @DisplayName("With single character, peeking character should not affect consuming character.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_peekShouldNotAffectGet(char testCharacter) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        testSubject.peekNext();
        testSubject.peekNext();
        Optional<CodeCharacter> character = testSubject.getNext();
        assertTrue(character.isPresent());
        assertEquals(testCharacter, character.get().getValue());
    }

    @DisplayName("With single character, peeking should be idempotent.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_peekShouldBeIdempotent(char testCharacter) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        assertTrue(testSubject.peekNext().isPresent());
        assertEquals(testCharacter, testSubject.peekNext().get().getValue());
        assertEquals(testCharacter, testSubject.peekNext().get().getValue());
        assertEquals(testCharacter, testSubject.peekNext().get().getValue());
        assertEquals(testCharacter, testSubject.peekNext().get().getValue());
        assertEquals(testCharacter, testSubject.peekNext().get().getValue());
    }

    @DisplayName("With single character, after reaching EOF, current line should be first line.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_currentLineShouldBeFirstLineAfterEOF(char testCharacter) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        assertEquals(testCharacter, testSubject.getNext().get().getValue());
        assertFalse(testSubject.getNext().isPresent());

        assertEquals(0, testSubject.getCurrentLine().get().getLineNumber());
        assertEquals(1, testSubject.getCurrentLine().get().getSize());
        assertEquals(String.valueOf(testCharacter), testSubject.getCurrentLine().get().getLineAsString());
    }

    @DisplayName("With single character, peeking should return empty optional after getting first character.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_shouldPeekEmptyAfterGetting(char testCharacter) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        assertEquals(testCharacter, testSubject.getNext().get().getValue());
        assertFalse(testSubject.peekNext().isPresent());
    }

    @DisplayName("With two characters, get them and return empty.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" string")
    @ValueSource(strings = {"a$", "@b", "\t\t", "  "})
    default void twoCharacters_shouldPeekEmptyAfterGetting(String testString) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(testString);

        assertEquals(testString.charAt(0), testSubject.getNext().get().getValue());
        assertEquals(testString.charAt(1), testSubject.getNext().get().getValue());
        assertFalse(testSubject.getNext().isPresent());
    }

    @DisplayName("With two characters, peeking characters before consuming should not be a problem.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" string")
    @ValueSource(strings = {"a$", "@b", "\t\t", "  "})
    default void twoCharacters_peekingBeforeGetting(String testString) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(testString);

        assertEquals(testString.charAt(0), testSubject.peekNext().get().getValue());
        assertEquals(testString.charAt(0), testSubject.getNext().get().getValue());
        assertEquals(testString.charAt(1), testSubject.peekNext().get().getValue());
        assertEquals(testString.charAt(1), testSubject.getNext().get().getValue());
        assertFalse(testSubject.peekNext().isPresent());
        assertFalse(testSubject.getNext().isPresent());
    }

    @DisplayName("With two lines, should return '\\n' at the end of first one.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_shouldReturnNewLineCharacter(String firstLine, String secondLine) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.getNext().get().getValue());
        }
        // Read new line
        assertEquals('\n', testSubject.getNext().get().getValue());
    }

    @DisplayName("With two lines, peeking '\\n' should not affect consuming '\\n'.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_peekNewLineThenConsumeNewLine(String firstLine, String secondLine) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.getNext().get().getValue());
        }

        assertEquals('\n', testSubject.peekNext().get().getValue());
        assertEquals('\n', testSubject.getNext().get().getValue());
    }

    @DisplayName("With two lines, after consuming new line, should peek first of second line.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLine_shouldPeekFirstFromSecondLine(String firstLine, String secondLine) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.getNext().get().getValue());
        }
        assertEquals('\n', testSubject.getNext().get().getValue());

        assertEquals(secondLine.charAt(0), testSubject.peekNext().get().getValue());
    }

    @DisplayName("With two lines, after consuming new line, peeking should not affect consuming first character of second line.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLine_peekShouldNotAffectGettingFirstFromSecondLine(String firstLine, String secondLine) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.getNext().get().getValue());
        }
        assertEquals('\n', testSubject.getNext().get().getValue());

        assertEquals(secondLine.charAt(0), testSubject.peekNext().get().getValue()); // Here we peek first from second line
        assertEquals(secondLine.charAt(0), testSubject.getNext().get().getValue());
    }

    @DisplayName("With two lines, after consuming new line and peeking, current line should still be first.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLineAndPeek_currentLineShouldBeFirst(String firstLine, String secondLine) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.getNext().get().getValue());
        }
        assertEquals('\n', testSubject.getNext().get().getValue());
        assertEquals(secondLine.charAt(0), testSubject.peekNext().get().getValue()); // Here we peek first from second line

        assertEquals(firstLine, testSubject.getCurrentLine().get().getLineAsStringWithoutSeparator());
    }

    @DisplayName("With two lines, after consuming new line, should have next character.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLine_shouldHaveNextCharacter(String firstLine, String secondLine) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.getNext().get().getValue());
        }
        assertEquals('\n', testSubject.getNext().get().getValue());

        assertTrue(testSubject.hasNext());
    }

    @DisplayName("With two lines, after consuming first of second line, current line should be second.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterFirstFromSecond_currentLineShouldBeSecond(String firstLine, String secondLine) throws IOException {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.getNext().get().getValue());
        }
        assertEquals('\n', testSubject.getNext().get().getValue());
        assertEquals(secondLine.charAt(0), testSubject.getNext().get().getValue());

        assertEquals(secondLine, testSubject.getCurrentLine().get().getLineAsString());
    }

    static Stream<Arguments> twoLinesTestCases(){
        return Stream.of(
                Arguments.of("first","second"), // Common chars
                Arguments.of("\t","second"),    // First tab
                Arguments.of("first","\t"),     // Second tab
                Arguments.of("\t","\t"),        // Both tab
                Arguments.of(" ","second"),     // First whitespace
                Arguments.of("first"," "),      // Second whitespace
                Arguments.of(" "," "),          // Both whitespace
                Arguments.of("","second")       // First blank line
        );
    }
}