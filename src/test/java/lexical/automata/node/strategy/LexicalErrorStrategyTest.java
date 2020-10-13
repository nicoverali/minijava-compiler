package lexical.automata.node.strategy;

import io.code.CodeCharacter;
import io.code.CodeLine;
import org.junit.jupiter.api.Test;
import sequence.MarkableAppendableSequenceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

// TODO We should first define what we expect from exceptions, do them have to contain the character that produces the error ?
class LexicalErrorStrategyTest {

    private static final String ERROR_MSG = "This is an error message";

    LexicalErrorStrategy<Integer> testSubject = new LexicalErrorStrategy<>(ERROR_MSG);

    @Test
    void onNoBranchSelected_always_returnsNull(){

    }

    @Test
    void onEndOfFile_lineIsNull_returnsNull(){

    }

    @Test
    void onEndOfFile_lineIsNotNull_returnsNull(){

    }

}