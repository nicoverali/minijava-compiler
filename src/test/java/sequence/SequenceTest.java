package sequence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public interface SequenceTest {

    @DisplayName("With empty sequence, should return empty optional on get")
    @Test
    void onEmptySequence_shouldReturnEmptyOptional_onGet();

    @DisplayName("With empty sequence, should return empty optional on peek")
    @Test
    void onEmptySequence_shouldReturnEmptyOptional_onPeek();

    @DisplayName("With empty sequence, should return false on hasNext")
    @Test
    void onEmptySequence_shouldReturnFalse_onHasNext();

    @DisplayName("With single element, should return the element on get")
    @Test
    void singleElement_shouldReturnElement_onGet();

    @DisplayName("With single element, should return the element on peek")
    @Test
    void singleElement_shouldReturnElement_onPeek();

    @DisplayName("With single element, should return true on hasNext")
    @Test
    void singleElement_shouldReturnElement_onHasNext();

    @DisplayName("With two elements, after getting first, should return second the element on get")
    @Test
    void twoElements_afterGettingFirst_shouldReturnSecond_onGet();

    @DisplayName("With two elements, after getting first, should return second the element on peek")
    @Test
    void twoElements_afterGettingFirst_shouldReturnSecond_onPeek();

    @DisplayName("With two elements, after getting first, should return true on hasNext")
    @Test
    void twoElements_afterGettingFirst_shouldReturnTrue_onHasNext();

    @DisplayName("With two elements, after getting both, should return empty Optional on get")
    @Test
    void twoElements_afterGettingBoth_shouldReturnEmptyOptional_onGet();

    @DisplayName("With two elements, after getting both, should return empty Optional on peek")
    @Test
    void twoElements_afterGettingBoth_shouldReturnEmptyOptional_onPeek();

    @DisplayName("With two elements, after getting both, should return false on hasNext")
    @Test
    void twoElements_afterGettingBoth_shouldReturnFalse_onHasNext();

    @DisplayName("Single elements, peeking should not affect get")
    @Test
    void singleElement_peeking_shouldNotAffectGet();

    @DisplayName("Two elements, peeking should not affect get")
    @Test
    void twoElement_peeking_shouldNotAffectGet();


}