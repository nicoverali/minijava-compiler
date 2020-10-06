package io.code.reader;

import io.code.CodeLineFactory;
import io.code.DefaultCodeLineFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BufferedCodeLinesReaderTest implements CodeLinesReaderTest{

    @Mock BufferedReader buffReader;

    /**
     * Mock {@link BufferedReader} and provide {@link DefaultCodeLineFactory}.
     * We assume <code>DefaultCodeLine</code> works correctly because its tested in {@link io.code.DefaultCodeLineTest}
     *
     * @see io.code.DefaultCodeLineTest
     */
    @Override
    public CodeLinesReader createCodeLinesReader(String... lines) {
        CodeLineFactory lineFactory = new DefaultCodeLineFactory();
        BufferedReader bufferedReader = prepareBufferedReader(lines);
        return new BufferedCodeLinesReader(bufferedReader, lineFactory);
    }

    private BufferedReader prepareBufferedReader(String... lines){
        if (lines.length > 0 && lines[lines.length-1].equals("")){
            lines = Arrays.copyOfRange(lines, 0, lines.length-1); // Remove last blank line
        }

        try {
            if (lines.length == 0){
                when(buffReader.readLine()).thenReturn(null);
            } else if (lines.length == 1){
                when(buffReader.readLine()).thenReturn(lines[0], (String) null);
            } else {
                String[] linesWithoutFirst = Arrays.copyOfRange(lines, 1, lines.length+1);
                when(buffReader.readLine()).thenReturn(lines[0], linesWithoutFirst);
            }
            return buffReader;
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}