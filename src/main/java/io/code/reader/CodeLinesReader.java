package io.code.reader;

import io.code.CodeLine;
import sequence.Sequence;

import java.io.UncheckedIOException;
import java.util.Optional;

/**
 * This class provides a line-by-line abstraction over a file.
 * <br>
 * Every line of a file is represented as a {@link CodeLine}
 * When created it sets a cursor to the first line, and on every
 * call to {@link #next()} the cursor moves forward.
 */
public interface CodeLinesReader extends Sequence<CodeLine> {

    /**
     * Returns the current next line in the file, and moves to the cursor forward by one line.
     * <br><br>
     * If it's not the last line of the file, it will include
     * the line terminator ('\n').
     * <br><br>
     * Once the cursor reaches the end of the file it returns an empty {@link Optional}.
     *
     * @return an {@link Optional} wrapping the next line of the file
     * @throws UncheckedIOException if an I/O error occurs
     */
    @Override
    Optional<CodeLine> next() throws UncheckedIOException;

    /**
     * Checks whether the file has at least on more line, or the cursor has reach the end of the file
     *
     * @return true if there is a next line, false if the cursor has reach the end
     * @throws UncheckedIOException if an I/O error occurs
     */
    @Override
    boolean hasNext() throws UncheckedIOException;

    /**
     * Returns the next line in the file but does not consume it, that is, it does not move the cursor.
     * <br><br>
     * The returned line will have a line terminator if it's not the last line, just like {@link #next()}  does.
     *
     * @return an {@link Optional} wrapping the next line of the file
     * @throws UncheckedIOException if an I/O error occurs
     */
    @Override
    Optional<CodeLine> peek() throws UncheckedIOException;

}
