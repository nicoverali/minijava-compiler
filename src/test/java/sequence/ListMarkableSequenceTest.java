package sequence;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ListMarkableSequenceTest implements MarkableAppendableSequenceTest {

    private static final Integer FIRST_ELEMENT = -5;
    private static final Integer SECOND_ELEMENT = 0;
    private static final Integer THIRD_ELEMENT = 5;

    ListMarkableSequence<Integer> testSubject = new ListMarkableSequence<>();

    @Override
	@Test
    public void mark_thenAppendAndGet_reset_shouldGetAppendedElementAgain() {
        testSubject.mark(1);
        testSubject.append(FIRST_ELEMENT);
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        testSubject.reset();
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertFalse(testSubject.hasNext());
    }

    @Override
	@Test
    public void emptySequence_appendElement_thenRetrieveSameElement_onGet() {
        testSubject.append(FIRST_ELEMENT);
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
    }

    @Override
	@Test
    public void emptySequence_appendElement_thenRetrieveSameElement_onPeek() {
        testSubject.append(FIRST_ELEMENT);
        assertEquals(FIRST_ELEMENT, testSubject.peek().get());
    }

    @Override
	@Test
    public void emptySequence_appendElement_thenShouldBeTrueHasNext() {
        testSubject.append(FIRST_ELEMENT);
        assertTrue(testSubject.hasNext());
    }

    @Override
	@Test
    public void emptySequence_appendListOfElements_thenGetThem() {
        testSubject.appendAll(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT, THIRD_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
        assertEquals(THIRD_ELEMENT, testSubject.next().get());
    }

    @Override
	@Test
    public void appendNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> testSubject.append(null));
    }

    @Override
	@Test
    public void emptySequence_appendListTwoElementsAndNull_shouldIgnoreNull() {
        testSubject.appendAll(Lists.newArrayList(FIRST_ELEMENT, null, THIRD_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(THIRD_ELEMENT, testSubject.next().get());
        assertFalse(testSubject.next().isPresent());
        assertFalse(testSubject.peek().isPresent());
        assertFalse(testSubject.hasNext());
    }

    @Override
	@Test
    public void emptySequence_appendListOfNull_shouldIgnoreAndKeepEmpty() {
        testSubject.appendAll(Lists.newArrayList(null, null, null, null));
        assertFalse(testSubject.next().isPresent());
        assertFalse(testSubject.peek().isPresent());
        assertFalse(testSubject.hasNext());
    }

    @Override
	@Test
    public void emptySequence_resetShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> testSubject.reset());
    }

    @Override
	@Test
    public void markWithNegativeLimit_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> testSubject.mark(-1));
    }

    @Override
	@Test
    public void emptySequence_markThenReset_shouldNotThrowException() {
        testSubject.mark(0);
        assertDoesNotThrow(() -> testSubject.reset());
    }

    @Override
	@Test
    public void twoElements_mark_getTheTwo_reset_shouldGetTheTwoAgain() {
        testSubject.appendAll(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        testSubject.mark(2);
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());

        testSubject.reset();
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
    }

    @Override
	@Test
    public void twoElements_getFirst_mark_getSecond_reset_shouldGetSecondAgain() {
        testSubject.appendAll(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        testSubject.mark(1);
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
        testSubject.reset();
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
    }

    @Override
	@Test
    public void twoElements_interlaceMarkWithGet_shouldResetThreeTimes_thenThrowException() {
        testSubject.appendAll(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        testSubject.mark(2);
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        testSubject.mark(1);
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
        testSubject.mark(0);

        assertDoesNotThrow(() -> testSubject.reset());
        assertDoesNotThrow(() -> testSubject.reset());
        assertDoesNotThrow(() -> testSubject.reset());
        assertThrows(IllegalStateException.class, () -> testSubject.reset());
    }

    @Override
    @Test
    public void markWithLimitFour_getSixElements_flushMarker_thenShouldGetTheSeventhElement() {
        IntStream.range(0,10).forEach(v -> testSubject.append(v));

        testSubject.mark(4);
        IntStream.range(0,7).forEach(v -> testSubject.next());
        testSubject.flushMarkers();
        assertEquals(7, testSubject.next().get());
    }

    @Override
	@Test
    public void markWithLimitFive_getSixElements_flushMarkers_thenResetShouldThrowException() {
        IntStream.range(0,6).forEach(v -> testSubject.append(v));

        testSubject.mark(5);
        IntStream.range(0,6).forEach(v -> testSubject.next());
        testSubject.flushMarkers();
        assertThrows(IllegalStateException.class, () -> testSubject.reset());
    }

    @Override
	@Test
    public void markWithLimitFive_getTwo_markWithLimitFour_getThree_flushMarkers_shouldResetToSecondMark_thenThrowException() {
        IntStream.range(0,10).forEach(v -> testSubject.append(v));
        testSubject.mark(5);
        IntStream.range(0,3).forEach(v -> testSubject.next());
        testSubject.mark(4);
        IntStream.range(0,4).forEach(v -> testSubject.next());

        testSubject.flushMarkers();
        assertDoesNotThrow(() -> testSubject.reset());
        assertEquals(3, testSubject.peek().get());
        assertThrows(IllegalStateException.class, () -> testSubject.reset());

    }

    @Override
	@Test
    public void onEmptySequence_shouldReturnEmptyOptional_onGet() {
        assertFalse(testSubject.next().isPresent());
    }

    @Override
	@Test
    public void onEmptySequence_shouldReturnEmptyOptional_onPeek() {
        assertFalse(testSubject.peek().isPresent());
    }

    @Override
	@Test
    public void onEmptySequence_shouldReturnFalse_onHasNext() {
        assertFalse(testSubject.hasNext());
    }

    @Override
	@Test
    public void singleElement_shouldReturnElement_onGet() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
    }

    @Override
	@Test
    public void singleElement_shouldReturnElement_onPeek() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.peek().get());
    }

    @Override
	@Test
    public void singleElement_shouldReturnElement_onHasNext() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT));
        assertTrue(testSubject.hasNext());
    }

    @Override
	@Test
    public void twoElements_afterGettingFirst_shouldReturnSecond_onGet() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
    }

    @Override
	@Test
    public void twoElements_afterGettingFirst_shouldReturnSecond_onPeek() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.peek().get());
    }

    @Override
	@Test
    public void twoElements_afterGettingFirst_shouldReturnTrue_onHasNext() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertTrue(testSubject.hasNext());
    }

    @Override
	@Test
    public void twoElements_afterGettingBoth_shouldReturnEmptyOptional_onGet() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
        assertFalse(testSubject.next().isPresent());
    }

    @Override
	@Test
    public void twoElements_afterGettingBoth_shouldReturnEmptyOptional_onPeek() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
        assertFalse(testSubject.peek().isPresent());
    }

    @Override
	@Test
    public void twoElements_afterGettingBoth_shouldReturnFalse_onHasNext() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
        assertFalse(testSubject.hasNext());
    }

    @Override
	@Test
    public void singleElement_peeking_shouldNotAffectGet() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.peek().get());
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
    }

    @Override
	@Test
    public void twoElement_peeking_shouldNotAffectGet() {
        testSubject = new ListMarkableSequence<>(Lists.newArrayList(FIRST_ELEMENT, SECOND_ELEMENT));
        assertEquals(FIRST_ELEMENT, testSubject.peek().get());
        assertEquals(FIRST_ELEMENT, testSubject.next().get());
        assertEquals(SECOND_ELEMENT, testSubject.peek().get());
        assertEquals(SECOND_ELEMENT, testSubject.next().get());
    }
}