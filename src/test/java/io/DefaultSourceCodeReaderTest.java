package io;

import io.code.CodeLineFactory;
import io.code.CodeLine;
import io.code.CodeCharacter;
import io.code.DefaultCodeLineFactory;
import io.code.reader.DefaultSourceCodeReader;
import io.code.reader.SourceCodeReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class will focus on testing a {@link SourceCodeReader} class, but it will also test
 * that the associated {@link CodeLine} and {@link CodeCharacter} classes are working correctly
 */
class DefaultSourceCodeReaderTest {

    private static String IO_TEST_01_PATH;
    private static String IO_TEST_02_PATH;

    private final CodeLineFactory lineFactory = new DefaultCodeLineFactory();

    @BeforeAll
    public static void setUp() {
        ClassLoader loader = DefaultSourceCodeReader.class.getClassLoader();
        IO_TEST_01_PATH = loader.getResource("source_code_tests/io_test_01.mj").getPath();
        IO_TEST_02_PATH = loader.getResource("source_code_tests/io_test_02.mj").getPath();
    }

    private void testCharacter(CodeCharacter ch, char value, CodeLine line, int columnNumber) {
        assertTrue(ch.equals(value));
        assertEquals(line, ch.getCodeLine());
        assertEquals(line.getLineNumber(), ch.getLineNumber());
        assertEquals(columnNumber, ch.getColumnNumber());
    }

    /**
     * This is a simple file to test basic char and new line characters recognition.
     * At the end of the file there's a "\n" string that should be consider as two separate
     * characters.
     */
    @Test
    public void io_test_01() throws IOException {
        SourceCodeReader reader = new DefaultSourceCodeReader(IO_TEST_01_PATH, lineFactory);
        CodeCharacter currentChar;


        testCharacter(reader.getNextCharacter(), 'A', reader.getCurrentLine(), 1);
        testCharacter(reader.getNextCharacter(), '.', reader.getCurrentLine(), 2);
        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 3);
        assertEquals("A.", reader.getCurrentLine().toString());
        assertEquals(2, reader.getCurrentLine().getSize());

        testCharacter(reader.getNextCharacter(), 'b', reader.getCurrentLine(), 1);
        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 2);
        assertEquals("b", reader.getCurrentLine().toString());
        assertEquals(1, reader.getCurrentLine().getSize());

        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 1);
        assertEquals("", reader.getCurrentLine().toString());
        assertEquals(0, reader.getCurrentLine().getSize());

        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 1);
        assertEquals("", reader.getCurrentLine().toString());
        assertEquals(0, reader.getCurrentLine().getSize());

        testCharacter(reader.getNextCharacter(), '@', reader.getCurrentLine(), 1);
        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 2);
        assertEquals("@", reader.getCurrentLine().toString());
        assertEquals(1, reader.getCurrentLine().getSize());

        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 1);
        assertEquals("", reader.getCurrentLine().toString());
        assertEquals(0, reader.getCurrentLine().getSize());

        testCharacter(reader.getNextCharacter(), '\\', reader.getCurrentLine(), 1);
        testCharacter(reader.getNextCharacter(), 'n', reader.getCurrentLine(), 2);
        assertEquals("\\n", reader.getCurrentLine().toString());
        assertEquals(2, reader.getCurrentLine().getSize());

        assertNull(reader.getNextCharacter());
    }

    /**
     * This file is similar to real source code. At the end of the file
     * there is a blank line that should be omitted.
     */
    @Test
    public void io_test_02() throws IOException {
        SourceCodeReader reader = new DefaultSourceCodeReader(IO_TEST_02_PATH, lineFactory);

        testCharacter(reader.getNextCharacter(), 'w', reader.getCurrentLine(), 1);
        testCharacter(reader.getNextCharacter(), 'h', reader.getCurrentLine(), 2);
        testCharacter(reader.getNextCharacter(), 'i', reader.getCurrentLine(), 3);
        testCharacter(reader.getNextCharacter(), 'l', reader.getCurrentLine(), 4);
        testCharacter(reader.getNextCharacter(), 'e', reader.getCurrentLine(), 5);
        testCharacter(reader.getNextCharacter(), '(', reader.getCurrentLine(), 6);
        testCharacter(reader.getNextCharacter(), 'T', reader.getCurrentLine(), 7);
        testCharacter(reader.getNextCharacter(), 'r', reader.getCurrentLine(), 8);
        testCharacter(reader.getNextCharacter(), 'u', reader.getCurrentLine(), 9);
        testCharacter(reader.getNextCharacter(), 'e', reader.getCurrentLine(), 10);
        testCharacter(reader.getNextCharacter(), ')', reader.getCurrentLine(), 11);
        testCharacter(reader.getNextCharacter(), ' ', reader.getCurrentLine(), 12);
        testCharacter(reader.getNextCharacter(), '{', reader.getCurrentLine(), 13);
        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 14);
        assertEquals("while(True) {", reader.getCurrentLine().toString());
        assertEquals(13, reader.getCurrentLine().getSize());

        testCharacter(reader.getNextCharacter(), '\t', reader.getCurrentLine(), 1);
        testCharacter(reader.getNextCharacter(), 'p', reader.getCurrentLine(), 2);
        testCharacter(reader.getNextCharacter(), 'r', reader.getCurrentLine(), 3);
        testCharacter(reader.getNextCharacter(), 'i', reader.getCurrentLine(), 4);
        testCharacter(reader.getNextCharacter(), 'n', reader.getCurrentLine(), 5);
        testCharacter(reader.getNextCharacter(), 't', reader.getCurrentLine(), 6);
        testCharacter(reader.getNextCharacter(), '(', reader.getCurrentLine(), 7);
        testCharacter(reader.getNextCharacter(), '"', reader.getCurrentLine(), 8);
        testCharacter(reader.getNextCharacter(), 'H', reader.getCurrentLine(), 9);
        testCharacter(reader.getNextCharacter(), 'i', reader.getCurrentLine(), 10);
        testCharacter(reader.getNextCharacter(), '"', reader.getCurrentLine(), 11);
        testCharacter(reader.getNextCharacter(), ')', reader.getCurrentLine(), 12);
        testCharacter(reader.getNextCharacter(), '\n', reader.getCurrentLine(), 13);
        assertEquals("\tprint(\"Hi\")", reader.getCurrentLine().toString());
        assertEquals(12, reader.getCurrentLine().getSize());

        // This should be the last character in the file, since the next line
        // is a blank line and should be omitted
        testCharacter(reader.getNextCharacter(), '}', reader.getCurrentLine(), 1);
        assertEquals("}", reader.getCurrentLine().toString());
        assertEquals(1, reader.getCurrentLine().getSize());

        assertNull(reader.getNextCharacter());
    }
}