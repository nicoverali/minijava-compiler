package io.code;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.Iterables;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

interface CodeLineTest<T extends CodeLine> {

    /**
     * Implement this to test {@link CodeLine} implementations
     */
    T createCodeLine(String line, int lineNumber);

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldNotHaveLineSeparatorByDefault(String lineTestCase, int lineNumber){
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);

        CodeCharacter lastCharacter = Iterables.getLast(testSubject.getAllCharacters(), null);
        assertTrue(lastCharacter == null || lastCharacter.getValue() != '\n' || lastCharacter.getValue() != '\r');
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldAddLineSeparatorOnlyOnce(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);

        // Verify size increases only by one
        int currentSize = testSubject.getSize();
        testSubject.addLineSeparator();
        assertEquals(currentSize+1, testSubject.getSize());
        testSubject.addLineSeparator();
        assertEquals(currentSize+1, testSubject.getSize());

        // Verify last character is actually a line separator
        CodeCharacter lastCharacter = Iterables.getLast(testSubject.getAllCharacters(), null);
        assertNotNull(lastCharacter);
        assertTrue(lastCharacter.getValue() == '\n'|| lastCharacter.getValue() ==  '\r');
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldRemoveLineSeparator(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);

        testSubject.addLineSeparator();
        int currentSize = testSubject.getSize();

        // Verify size decreases only by one
        testSubject.removeLineSeparator();
        assertEquals(currentSize-1, testSubject.getSize());
        testSubject.removeLineSeparator();
        assertEquals(currentSize-1, testSubject.getSize());

        // Verify that now last character is not a line terminator
        CodeCharacter lastCharacter = Iterables.getLast(testSubject.getAllCharacters(), null);
        assertTrue(lastCharacter == null || lastCharacter.getValue() != '\n'|| lastCharacter.getValue() !=  '\r');

    }

    @ParameterizedTest(name = "[{index}] At line {1}")
    @MethodSource("lineTestCases")
    default void lineNumberShouldBeTheOneAssigned(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        assertEquals(lineNumber, testSubject.getLineNumber());
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void listOfAllCharactersShouldBeEqualToLineTestCase(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        List<CodeCharacter> characters = testSubject.getAllCharacters();

        assertEquals(lineTestCase.length(), characters.size());

        for (int i = 0; i < characters.size(); i++){
            assertEquals(lineTestCase.charAt(i), characters.get(i).getValue());
        }
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldReturnIndividualCharactersCorrectly(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        for (int i = 0; i < lineTestCase.length(); i++){
            assertEquals(lineTestCase.charAt(i), testSubject.getCharacterAt(i).getValue());
        }
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void ifNotBlank_shouldReturnFirstCharacter(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        if (!lineTestCase.isEmpty()){
            assertTrue(testSubject.getFirstCharacter().isPresent());
        }
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void ifBlank_shouldNotReturnFirstCharacter(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        if (lineTestCase.isEmpty()){
            assertFalse(testSubject.getFirstCharacter().isPresent());
        }
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void sizeShouldBeGivenCorrectly(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        assertEquals(lineTestCase.length(), testSubject.getSize());
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldIterateThroughAllCharacters(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        Iterator<CodeCharacter> iterator = testSubject.iterator();

        int currentIdx = 0;
        while (iterator.hasNext()){
            assertEquals(lineTestCase.charAt(currentIdx), iterator.next().getValue());
            currentIdx++;
        }

        assertEquals(currentIdx, lineTestCase.length()); // Last index should be at the end of line test case

    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldGiveLineAsStringWithSeparator(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        testSubject.addLineSeparator();

        String lineAsString = testSubject.toString();
        assertTrue(lineAsString.equals(lineTestCase + '\n') || lineAsString.equals(lineTestCase + '\r'));
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldReturnLineSeparatorCharacter(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        testSubject.addLineSeparator();

        CodeCharacter lastCharacter = testSubject.getCharacterAt(testSubject.getSize()-1);
        assertNotNull(lastCharacter);
        assertTrue(lastCharacter.getValue() == '\n' || lastCharacter.getValue() == '\r');
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \" at line {1}")
    @MethodSource("lineTestCases")
    default void shouldReturnLineSeparatorCharacterOnIteration(String lineTestCase, int lineNumber) {
        CodeLine testSubject = createCodeLine(lineTestCase, lineNumber);
        testSubject.addLineSeparator();
        CodeCharacter lastCharacter = null;

        for (CodeCharacter ch : testSubject){
            lastCharacter = ch;
        }

        assertNotNull(lastCharacter);
        assertTrue(lastCharacter.getValue() == '\n' || lastCharacter.getValue() == '\r');
    }

    @ParameterizedTest(name = "[{index}] With \" {0} \"")
    @MethodSource("lineTestCases")
    default void shouldThrowExceptionOnNegativeLineNumber(String lineTestCase) {
        assertThrows(IllegalArgumentException.class, () -> createCodeLine(lineTestCase, -1));
    }

    static Stream<Arguments> lineTestCases(){
        return Stream.of(
                Arguments.of("This is a simple line", 0),
                Arguments.of("(\\n, \\r, \\t, $, @)", 1),
                Arguments.of("", 5),
                Arguments.of("\\n", 10)
        );
    }


}