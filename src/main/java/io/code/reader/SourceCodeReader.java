package io.code.reader;

import io.code.CodeCharacter;
import io.code.CodeLine;

import java.io.IOException;

public interface SourceCodeReader {

    /**
     * Returns the next character in the associated source code. This will return a <b>\n</b> character if the next character
     * is a new line.
     * If there's a blank line at the end of the file it will be omitted.
     *
     * @return the next character in the source code
     * @throws IOException if an I/O error occur
     */
    CodeCharacter getNextCharacter() throws IOException;

    /**
     * Returns the line of the last character returned by {@link #getNextCharacter()}.
     * If {@link #getNextCharacter()} has never been called, then it will return the first line of the source code.
     *
     * @return the line of the last character returned by this reader
     */
    CodeLine getCurrentLine();

}
