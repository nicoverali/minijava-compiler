package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OmitterEndStrategyTest {

    private OmitterEndStrategy testSubject = new OmitterEndStrategy();

    @Test
    void onNoBranchSelected_shouldNotThrowException() {
        assertDoesNotThrow(() -> testSubject.onNoBranchSelected(mock(CodeCharacter.class)));
    }

    @Test
    void onEndOfFile_shouldNotThrowException() {
        assertDoesNotThrow(() -> testSubject.onEndOfFile(null));
    }
}