package sequence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public interface AppendableSequenceTest extends SequenceTest {

    @DisplayName("With empty sequence, append single element and then get the same element")
    @Test
    void emptySequence_appendElement_thenRetrieveSameElement_onGet();

    @DisplayName("With empty sequence, append single element and then peek the same element")
    @Test
    void emptySequence_appendElement_thenRetrieveSameElement_onPeek();

    @DisplayName("With empty sequence, append single element, then should be true hasNext")
    @Test
    void emptySequence_appendElement_thenShouldBeTrueHasNext();

    @DisplayName("With empty sequence, append list of elements and then get them")
    @Test
    void emptySequence_appendListOfElements_thenGetThem();

    @DisplayName("Append null element, should throw exception")
    @Test
    void appendNull_shouldThrowException();

    @DisplayName("With empty sequence, append list of two elements and a null, should ignore null")
    @Test
    void emptySequence_appendListTwoElementsAndNull_shouldIgnoreNull();

    @DisplayName("With empty sequence, append list of nulls, should ignore them and keep empty")
    @Test
    void emptySequence_appendListOfNull_shouldIgnoreAndKeepEmpty();

}