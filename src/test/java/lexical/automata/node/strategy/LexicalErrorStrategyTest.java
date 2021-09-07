package lexical.automata.node.strategy;

import org.junit.jupiter.api.Test;

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