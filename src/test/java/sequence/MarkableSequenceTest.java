package sequence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public interface MarkableSequenceTest extends SequenceTest {

    @DisplayName("With empty sequence, reset should throw exception")
    @Test
    void emptySequence_resetShouldThrowException();

    @DisplayName("Mark with negative limit, should throw exception")
    @Test
    void markWithNegativeLimit_shouldThrowException();

    @DisplayName("With empty sequence, mark, then reset should not throw exception")
    @Test
    void emptySequence_markThenReset_shouldNotThrowException();

    @DisplayName("With two elements, mark, get the two elements, and reset, should get the two elements again")
    @Test
    void twoElements_mark_getTheTwo_reset_shouldGetTheTwoAgain();

    @DisplayName("With two elements, get first, mark, get second, and reset, should get only second again")
    @Test
    void twoElements_getFirst_mark_getSecond_reset_shouldGetSecondAgain();

    @DisplayName("With two elements, interlace mark with get, should reset three times, then throw exception")
    @Test
    void twoElements_interlaceMarkWithGet_shouldResetThreeTimes_thenThrowException();

    @DisplayName("Mark with read ahead limit 5, get 6 elements, flush markers, then reset should throw exception")
    @Test
    void markWithLimitFive_getSixElements_flushMarkers_thenResetShouldThrowException();

    @DisplayName("Mark with read ahead limit 5, after getting 2 mark with limit 4, get 3 elements, flush markers, should reset to second mark, then throw exception")
    @Test
    void markWithLimitFive_getTwo_markWithLimitFour_getThree_flushMarkers_shouldResetToSecondMark_thenThrowException();
}