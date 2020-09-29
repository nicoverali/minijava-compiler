package io.code.reader;

import io.code.CodeCharacter;
import io.code.CodeLine;

import java.io.IOException;
import java.util.Optional;

/**
 * This class provides methods to retrieve {@link CodeCharacter}s from a source code file in a <b>sequential way</b>.
 * <br><br>
 * Since the source code is probably an actual file, this class relies heavily on I/O operations.
 */
public interface SourceCodeReader {

    /**
     * Returns the next character in the associated source code. This will also return line separator characters (e.g. '\n').
     * <br>
     * If the last line of the file has a newline character, it will be ignored, and treated as EOF.
     * Therefore, the last blank line will also be skipped.
     * <br><br>
     * Once the end of the file is reached, this method will return empty {@link Optional}s
     *
     * @return an {@link Optional} wrapping the next character in the source code
     * @throws IOException if an I/O error occur
     */
    Optional<CodeCharacter> getNext() throws IOException;

    /**
     * Checks whether there is at least on unread character in the source code, or it has reached the end of the file
     *
     * @return true if there is at least one unread character, false otherwise
     * @throws IOException if an I/O error occurs
     */
    boolean hasNext() throws IOException;

    /**
     * Returns the next character of the source code, as {@link #getNext()}, but it does not consume
     * the character.
     * <br>
     * Multiple calls in a row will return the same character.
     *
     * @see #getNext()
     *
     * @return an {@link Optional} wrapping the next character in the source code
     * @throws IOException if an I/O error occur
     */
    Optional<CodeCharacter> peekNext() throws IOException;

    /**
     * Returns the line of the last character returned by {@link #getNext()}.
     * If {@link #getNext()} has never been called, then it will return the first line of the source code (if present).
     *
     * @return an {@link Optional} wrapping the line of the last character returned by this reader
     */
    Optional<CodeLine> getCurrentLine();

}
