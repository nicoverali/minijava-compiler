package io.code.reader;

import io.code.CodeCharacter;
import io.code.CodeLine;
import io.code.DefaultCodeLineFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.when;

/**
 * This class will focus on testing a {@link SourceCodeReader} class, but it will also test
 * that the associated {@link CodeLine} and {@link CodeCharacter} classes are working correctly
 */

@ExtendWith(MockitoExtension.class)
class BufferedSourceCodeReaderTest implements SourceCodeReaderTest<BufferedSourceCodeReader> {

    @Mock BufferedReader buffReaderMock;

    /**
     * Mock {@link BufferedReader} and provide {@link DefaultCodeLineFactory}.
     * We assume <code>DefaultCodeLine</code> works correctly because its tested in {@link io.code.DefaultCodeLineTest}
     *
     * @see io.code.DefaultCodeLineTest
     */
    @Override
    public BufferedSourceCodeReader createSourceCodeReader(String... lines) throws IOException {
        // BufferedReader does not emit line separators and thus ignores any last blank line
        String[] formattedLines = addNullAtTheEnd(removeLastBlankLine(removeLineSeparator(lines)));

        if (formattedLines.length == 0){
            when(buffReaderMock.readLine()).thenReturn(null);
        } else if (formattedLines.length == 1){
            when(buffReaderMock.readLine()).thenReturn(formattedLines[0], (String) null);
        } else {
            String[] linesWithoutFirst = Arrays.copyOfRange(formattedLines, 1, formattedLines.length);
            when(buffReaderMock.readLine()).thenReturn(formattedLines[0], linesWithoutFirst);
        }

        return new BufferedSourceCodeReader(buffReaderMock, new DefaultCodeLineFactory());
    }

    private String[] addNullAtTheEnd(String... lines){
        String[] withNull = Arrays.copyOf(lines, lines.length+1);
        withNull[withNull.length-1] = null;
        return withNull;
    }

    private String[] removeLastBlankLine(String... lines){
        if (lines.length > 0 && lines[lines.length-1].isEmpty()){
            return Arrays.copyOfRange(lines, 0, lines.length-1);
        }
        return lines;
    }

    private String[] removeLineSeparator(String... lines){
        return Arrays.stream(lines)
                .map(line -> line.replaceAll("[\n\r]", ""))
                .toArray(String[]::new);
    }
}