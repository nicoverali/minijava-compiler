package lexical.automata.node.strategy;


import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import util.RoyOsheroveTestNameGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NullStrategyTest {

    NullStrategy testSubject = new NullStrategy();

    @Test
    void onNoBranchSelected_always_returnsNull(){
        assertNull(testSubject.onNoBranchSelected(mock(SourceCodeReader.class), mock(CodeCharacter.class)));
    }

    @Test
    void onEndOfFile_lineIsNull_returnsNull(){
        assertNull(testSubject.onEndOfFile(mock(SourceCodeReader.class), null));
    }

    @Test
    void onEndOfFile_lineIsNotNull_returnsNull(){
        assertNull(testSubject.onEndOfFile(mock(SourceCodeReader.class), mock(CodeLine.class)));
    }

}