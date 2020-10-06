package sequence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public interface MarkableAppendableSequenceTest extends AppendableSequenceTest, MarkableSequenceTest{

    @DisplayName("Mark, then append and get, reset, should get appended element again")
    @Test
    void mark_thenAppendAndGet_reset_shouldGetAppendedElementAgain();

}
