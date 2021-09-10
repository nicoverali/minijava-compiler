package lexical.automata.node;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.SourceCodeReader;
import lexical.LexicalException;
import lexical.Token;
import lexical.TokenType;
import lexical.automata.Lexeme;
import lexical.automata.NodeBranch;
import lexical.automata.node.strategy.AcceptorStrategy;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import util.RoyOsheroveTestNameGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(RoyOsheroveTestNameGenerator.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AcceptorStrategyTest {

    private static final String NAME = "This is a test strategy";
    private static final int LINE_NUMBER = 4;
    private static final int COL_NUMBER = 8;

    TokenType TOKEN_TYPE = TokenType.ASSIGN;
    @Mock Token RESULT;
    @Mock SourceCodeReader readerMock;
    @Mock CodeLine lineMock;
    @Mock CodeCharacter charMock;

    @Mock LexicalException exceptionMock;

    @Mock NodeBranch branchMock;


    @Test
    void process_emptyFile_returnNull(){
        AcceptorStrategy strategy = new AcceptorStrategy();
        Token token = strategy.onEndOfFile(readerMock, Lexeme.empty());
        assertNull(token);
    }

    @Test
    void process_emptyFileWithAssignedToken_returnToken(){
        AcceptorStrategy strategy = new AcceptorStrategy(TOKEN_TYPE);
        Token token = strategy.onEndOfFile(readerMock, Lexeme.empty());
        assertNotNull(token);
        assertEquals(TOKEN_TYPE, token.getType());
    }

    @Test
    void process_emptyFileWithAssignedToken_setTokenLineTo0(){
        AcceptorStrategy strategy = new AcceptorStrategy(TOKEN_TYPE);
        Token token = strategy.onEndOfFile(readerMock, Lexeme.empty());
        assertTrue(token.getFirstLine().isEmpty());
        assertEquals(0, token.getLineNumber());
    }

    @Test
    void process_emptyFileWithAssignedToken_setTokenColumnTo0(){
        AcceptorStrategy strategy = new AcceptorStrategy(TOKEN_TYPE);
        Token token = strategy.onEndOfFile(readerMock, Lexeme.empty());
        assertTrue(token.getFirstCharacter().isEmpty());
        assertEquals(0, token.getColumnNumber());
    }

    @Test
    void process_endOfFileWithToken_returnTokenWithLastLine(){
        when(readerMock.getLastLine()).thenReturn(Optional.of(lineMock));
        when(lineMock.getLineNumber()).thenReturn(LINE_NUMBER);

        AcceptorStrategy strategy = new AcceptorStrategy(TOKEN_TYPE);
        Token token = strategy.onEndOfFile(readerMock, Lexeme.empty());

        assertTrue(token.getFirstLine().isPresent());
        assertEquals(lineMock, token.getFirstLine().get());
        assertEquals(LINE_NUMBER, token.getLineNumber());
    }

}