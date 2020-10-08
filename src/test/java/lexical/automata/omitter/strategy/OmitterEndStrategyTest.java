package lexical.automata.omitter.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OmitterEndStrategyTest {

    private final OmitterEndStrategy testSubject = new OmitterEndStrategy();

    @DisplayName("If no branch is selected, then should simply return without any exception")
    @Test
    void onNoBranchSelected_shouldNotThrowException() {
        assertDoesNotThrow(() -> testSubject.onNoBranchSelected(mock(CodeCharacter.class)));
    }

    @DisplayName("If reaches EOF, without current line, then should simply return without any exception")
    @Test
    void onEndOfFile_withoutLine_shouldNotThrowException() {
        assertDoesNotThrow(() -> testSubject.onEndOfFile(null));
    }

    @DisplayName("If reaches EOF, with a line, then should simply return without any exception")
    @Test
    void onEndOfFile_withLine_shouldNotThrowException() {
        assertDoesNotThrow(() -> testSubject.onEndOfFile(mock(CodeLine.class)));
    }
}