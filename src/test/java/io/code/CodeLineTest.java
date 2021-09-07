package io.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("Should allow line separators")
    @Test
    default void shouldAllowLineSeparators(){
        CodeLine testSubject = createCodeLine("test\n", 0);

        CodeCharacter lastCharacter = Iterables.getLast(testSubject.getAllCharacters(), null);
        assertEquals('\n', lastCharacter.getValue());
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