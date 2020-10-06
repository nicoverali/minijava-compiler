package io.code.reader;

import io.code.CodeCharacter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public interface SourceCodeReaderTest<T extends SourceCodeReader> {

    T createSourceCodeReader(String... lines);

    @DisplayName("With empty file, should return empty optional.")
    @Test
    default void onEmptyFile_shouldReturnEmptyCharacter()  {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("With empty file, repeat request, should always return empty optional.")
    @Test
    default void onEmptyFile_repeatRequest_shouldReturnEmptyCharacter()  {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.next().isPresent());
        assertFalse(testSubject.next().isPresent());
        assertFalse(testSubject.next().isPresent());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("With empty file, peeking should return empty optional.")
    @Test
    default void onEmptyFile_peekShouldReturnEmptyCharacter()  {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.peek().isPresent());
    }

    @DisplayName("With empty file, should not have current line.")
    @Test
    default void onEmptyFile_shouldNotHaveCurrentLine()  {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.getCurrentLine().isPresent());
    }

    @DisplayName("With empty file, should not have a next character.")
    @Test
    default void onEmptyFile_shouldNotHaveNext()  {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertFalse(testSubject.hasNext());
    }

    @DisplayName("With empty file, line number should be 0.")
    @Test
    default void onEmptyFile_lineNumberShouldBe0()  {
        SourceCodeReader testSubject = createSourceCodeReader();
        assertEquals(0, testSubject.getCurrentLineNumber());
    }

    @DisplayName("With single character, should return character and then empty.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_shouldReturnEmptyCharacter(char testCharacter)  {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        // Verify first character
        Optional<CodeCharacter> character = testSubject.next();
        assertTrue(character.isPresent());
        assertEquals(testCharacter, character.get().getValue());

        // Verify end of file
        assertFalse(testSubject.next().isPresent());

    }

    @DisplayName("With single character, should peek first character.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_shouldPeekFirstCharacter(char testCharacter)  {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        Optional<CodeCharacter> character = testSubject.peek();
        assertTrue(character.isPresent());
        assertEquals(testCharacter, character.get().getValue());

    }

    @DisplayName("With single character, peeking character should not affect consuming character.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_peekShouldNotAffectGet(char testCharacter)  {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        testSubject.peek();
        testSubject.peek();
        Optional<CodeCharacter> character = testSubject.next();
        assertTrue(character.isPresent());
        assertEquals(testCharacter, character.get().getValue());
    }

    @DisplayName("With single character, peeking should be idempotent.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_peekShouldBeIdempotent(char testCharacter)  {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        assertTrue(testSubject.peek().isPresent());
        assertEquals(testCharacter, testSubject.peek().get().getValue());
        assertEquals(testCharacter, testSubject.peek().get().getValue());
        assertEquals(testCharacter, testSubject.peek().get().getValue());
        assertEquals(testCharacter, testSubject.peek().get().getValue());
        assertEquals(testCharacter, testSubject.peek().get().getValue());
    }

    @DisplayName("With single character, after reaching EOF, current line should be first line.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_currentLineShouldBeFirstLineAfterEOF(char testCharacter)  {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        assertEquals(testCharacter, testSubject.next().get().getValue());
        assertFalse(testSubject.next().isPresent());

        assertEquals(0, testSubject.getCurrentLine().get().getLineNumber());
        assertEquals(1, testSubject.getCurrentLine().get().getSize());
        assertEquals(String.valueOf(testCharacter), testSubject.getCurrentLine().get().toString());
    }

    @DisplayName("With single character, peeking should return empty optional after getting first character.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_shouldPeekEmptyAfterGetting(char testCharacter)  {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));

        assertEquals(testCharacter, testSubject.next().get().getValue());
        assertFalse(testSubject.peek().isPresent());
    }

    @DisplayName("With single character, line number should be 0.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" character")
    @ValueSource(chars = {'a', '@', '$', '"', '\t'})
    default void singleCharacter_lineNumberShouldBe0(char testCharacter)  {
        SourceCodeReader testSubject = createSourceCodeReader(String.valueOf(testCharacter));
        assertEquals(0, testSubject.getCurrentLineNumber());
    }

    @DisplayName("With two characters, get them and return empty.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" string")
    @ValueSource(strings = {"a$", "@b", "\t\t", "  "})
    default void twoCharacters_shouldPeekEmptyAfterGetting(String testString)  {
        SourceCodeReader testSubject = createSourceCodeReader(testString);

        assertEquals(testString.charAt(0), testSubject.next().get().getValue());
        assertEquals(testString.charAt(1), testSubject.next().get().getValue());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("With two characters, peeking characters before consuming should not be a problem.")
    @ParameterizedTest(name = "[{index}] With \"{0}\" string")
    @ValueSource(strings = {"a$", "@b", "\t\t", "  "})
    default void twoCharacters_peekingBeforeGetting(String testString)  {
        SourceCodeReader testSubject = createSourceCodeReader(testString);

        assertEquals(testString.charAt(0), testSubject.peek().get().getValue());
        assertEquals(testString.charAt(0), testSubject.next().get().getValue());
        assertEquals(testString.charAt(1), testSubject.peek().get().getValue());
        assertEquals(testString.charAt(1), testSubject.next().get().getValue());
        assertFalse(testSubject.peek().isPresent());
        assertFalse(testSubject.next().isPresent());
    }

    @DisplayName("With two lines, should return '\\n' at the end of first one.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_shouldReturnNewLineCharacter(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        // Read new line
        assertEquals('\n', testSubject.next().get().getValue());
    }

    @DisplayName("With two lines, peeking '\\n' should not affect consuming '\\n'.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_peekNewLineThenConsumeNewLine(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }

        assertEquals('\n', testSubject.peek().get().getValue());
        assertEquals('\n', testSubject.next().get().getValue());
    }

    @DisplayName("With two lines, after consuming new line, should peek first of second line.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLine_shouldPeekFirstFromSecondLine(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());

        assertEquals(secondLine.charAt(0), testSubject.peek().get().getValue());
    }

    @DisplayName("With two lines, after consuming new line, peeking should not affect consuming first character of second line.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLine_peekShouldNotAffectGettingFirstFromSecondLine(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());

        assertEquals(secondLine.charAt(0), testSubject.peek().get().getValue()); // Here we peek first from second line
        assertEquals(secondLine.charAt(0), testSubject.next().get().getValue());
    }

    @DisplayName("With two lines, after consuming new line and peeking, current line should still be first.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLineAndPeek_currentLineShouldBeFirst(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());
        assertEquals(secondLine.charAt(0), testSubject.peek().get().getValue()); // Here we peek first from second line

        assertEquals(firstLine+'\n', testSubject.getCurrentLine().get().toString());
    }

    @DisplayName("With two lines, after consuming new line, should have next character.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterNewLine_shouldHaveNextCharacter(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());

        assertTrue(testSubject.hasNext());
    }

    @DisplayName("With two lines, after consuming first of second line, current line should be second.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterFirstFromSecond_currentLineShouldBeSecond(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());
        assertEquals(secondLine.charAt(0), testSubject.next().get().getValue());

        assertEquals(secondLine, testSubject.getCurrentLine().get().toString());
    }

    @DisplayName("With two lines, without consuming, current line number should be 0.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_withoutConsuming_lineNumberShouldBeZero(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        assertEquals(0, testSubject.getCurrentLineNumber());
    }

    @DisplayName("With two lines, after consuming first, current line number should be 1.")
    @ParameterizedTest(name = "[{index}] With lines \"{0}\" and \"{1}\"")
    @MethodSource("twoLinesTestCases")
    default void twoLines_afterConsumingFirst_lineNumberShouldBeOne(String firstLine, String secondLine)  {
        SourceCodeReader testSubject = createSourceCodeReader(firstLine, secondLine);
        // Read only characters
        for (char ch : firstLine.toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());
        assertEquals(secondLine.charAt(0), testSubject.next().get().getValue());

        assertEquals(1, testSubject.getCurrentLineNumber());
    }


    @DisplayName("With three lines, second line is blank, get next at the end of first should return new line of second line.")
    @Test
    default void threeLines_secondIsBlank_getNextAtEndOfFirst_shouldReturnNewLineOfSecond()  {
        SourceCodeReader testSubject = createSourceCodeReader("First", "", "Third");
        // Read only characters
        for (char ch : "First".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());

        assertEquals('\n', testSubject.next().get().getValue());
    }

    @DisplayName("With three lines, second line is blank, peek next at the end of first should return new line of second line.")
    @Test
    default void threeLines_secondIsBlank_peekNextAtEndOfFirst_shouldReturnNewLineOfSecond()  {
        SourceCodeReader testSubject = createSourceCodeReader("First", "", "Third");
        // Read only characters
        for (char ch : "First".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertEquals('\n', testSubject.next().get().getValue());

        assertEquals('\n', testSubject.peek().get().getValue());
    }

    @DisplayName("With three lines, mark at the beginning, get all characters, reset, should get all twice.")
    @Test
    default void threeLines_markAtBeginning_resetAtEnd_shouldReadTwice()  {
        SourceCodeReader testSubject = createSourceCodeReader("First", "", "Third");
        testSubject.mark(100);

        for (char ch : "First\n\nThird".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertDoesNotThrow(testSubject::reset);
        for (char ch : "First\n\nThird".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
    }

    @DisplayName("With three lines, first two blanks, mark at the beginning, reset, should get all again.")
    @Test
    default void threeLines_firstTwoBlank_markAtTheBeginning_reset_shouldGetAllAgain()  {
        SourceCodeReader testSubject = createSourceCodeReader("", "", "T");

        testSubject.mark(10);
        assertEquals('\n', testSubject.next().get().getValue());
        testSubject.mark(10);
        assertEquals('\n', testSubject.next().get().getValue());
        testSubject.mark(10);
        assertEquals('T', testSubject.next().get().getValue());

        testSubject.reset();
        assertEquals('T', testSubject.next().get().getValue());
        testSubject.reset();
        assertEquals('\n', testSubject.next().get().getValue());
        testSubject.reset();
        assertEquals('\n', testSubject.next().get().getValue());

    }

    @DisplayName("With three lines, mark middle every line, reset and test for every mark")
    @Test
    default void threeLines_markMiddleOfEveryLine_resetAndTestEveryLine()  {
        SourceCodeReader testSubject = createSourceCodeReader("First", "Second", "Third");
        testSubject.mark(100);

        for (char ch : "Fir".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        testSubject.mark(50);
        for (char ch: "st\n".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        for (char ch : "Sec".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        testSubject.mark(50);
        for (char ch: "ond\n".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        for (char ch : "Thi".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        testSubject.mark(50);
        for (char ch: "rd".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertDoesNotThrow(testSubject::reset);
        for (char ch : "rd".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertDoesNotThrow(testSubject::reset);
        for (char ch : "ond\nThird".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
        assertDoesNotThrow(testSubject::reset);
        for (char ch : "st\nSecond\nThird".toCharArray()){
            assertEquals(ch, testSubject.next().get().getValue());
        }
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