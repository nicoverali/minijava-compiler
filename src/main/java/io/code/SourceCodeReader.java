package io.code;

import sequence.MarkableSequence;

import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * This class provides methods to retrieve {@link CodeCharacter}s from a source code file in a <b>sequential way</b>.
 * <br><br>
 * Since the source code is probably an actual file, this class relies heavily on I/O operations.
 */
public interface SourceCodeReader extends MarkableSequence<CodeCharacter> {

    /**
     * Returns the next character in the associated source code. This will also return line separator characters (e.g. '\n').
     * <br>
     * If the last line of the file has a newline character, it will be ignored, and treated as EOF.
     * Therefore, the last blank line will also be skipped.
     * <br><br>
     * Once the end of the file is reached, this method will return empty {@link Optional}s
     *
     * @return an {@link Optional} wrapping the next character in the source code
     * @throws UncheckedIOException if an I/O error occur
     */
    @Override
    Optional<CodeCharacter> next() throws UncheckedIOException;

    /**
     * Checks whether there is at least on unread character in the source code, or it has reached the end of the file
     *
     * @return true if there is at least one unread character, false otherwise
     * @throws UncheckedIOException if an I/O error occurs
     */
    @Override
    boolean hasNext() throws UncheckedIOException;

    /**
     * Returns the next character of the source code, as {@link #next()}, but it does not consume
     * the character.
     * <br>
     * Multiple calls in a row will return the same character.
     *
     * @see #next()
     *
     * @return an {@link Optional} wrapping the next character in the source code
     * @throws UncheckedIOException if an I/O error occur
     */
    @Override
    Optional<CodeCharacter> peek() throws UncheckedIOException;

    /**
     * Returns the line of the last character returned by {@link #next()}.
     * If {@link #next()} has never been called, then it will return the first line of the source code (if present).
     *
     * @return an {@link Optional} wrapping the line of the last character returned by this reader
     */
    Optional<CodeLine> getLastLine();

    /**
     * Returns the last character returned by {@link #next()}.
     * If {@link #next()} has never been called or the last call reach EOF, then it will return an empty {@link Optional}
     *
     * @return an {@link Optional} wrapping the last character returned by this reader
     */
    Optional<CodeCharacter> getLastCharacter();

    /**
     * Returns the line number of the last character returned by {@link #next()}, or line 0 if the either the
     * file is empty or no character have been requested yet
     *
     * @return current line number
     */
    int getCurrentLineNumber();

}
