package io.code;


public interface CodeLineFactory {

    /**
     * Creates a new {@link CodeLine}.
     * <br>
     * The given line can't contain line separators. Use {@link CodeLine#addLineSeparator()} instead.
     * <br>
     * Line number can't be negative
     *
     * <br><br>
     * If the given String is <code>null</code>, then a blank {@link CodeLine} will be returned.
     *
     * @param line String representation of the new line
     * @param lineNumber position of the new line in a file
     * @return a new {@link CodeLine} with the characters of <code>line</code>, and at <code>lineNumber</code>
     * @throws IllegalArgumentException if <code>lineNumber</code> is negative or <code>line</code> contains a line separator
     */
    CodeLine create(String line, int lineNumber) throws IllegalArgumentException;

}
