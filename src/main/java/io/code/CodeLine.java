package io.code;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * A <code>CodeLine</code> is consider to be a numbered sequence of {@link CodeCharacter}s. This sequence may be empty and
 * may or may not have a line separator (e.g. '\n') at the end. However, a <code>CodeLine</code> can't have a line
 * separator between two common characters.
 * <br><br>
 * A <code>CodeLine</code> belongs to a file and therefore has an associated line number.
 * <br>
 * Line numbers are zero-based indexed.
 */
public interface CodeLine extends Iterable<CodeCharacter> {

    /**
     * Adds a line separator at the end. If a line separator was already at the end, then calling this method
     * won't produce any effect.
     */
    void addLineSeparator();

    /**
     * Removes any line separator from the end of this lines. If there was no line separator at the end,
     * then calling this method won't produce any effect.
     */
    void removeLineSeparator();

    /**
     * Returns the position (row) of this line inside a file. Line numbers are zero-based indexed.
     *
     * @return the position of this line
     */
    int getLineNumber();

    /**
     * Returns a list of all the characters. This will also include any line separator that may
     * be present at the end of this line.
     *
     * @return a list of all the characters within this line, including line separators.
     */
    List<CodeCharacter> getAllCharacters();

    /**
     * Returns the character which position in this line is the same as the one given as argument.
     * <br>
     * Character's column numbers are zero-based indexed.
     *
     * @param column character's position
     * @return the character in this line with the given position
     * @throws IndexOutOfBoundsException if the given column number exceeds the size of this line
     */
    CodeCharacter getCharacterAt(int column) throws IndexOutOfBoundsException;

    /**
     * @return an {@link Optional} wrapping the first character of this line
     */
    Optional<CodeCharacter> getFirstCharacter();

    /**
     * @return the number of characters in this line
     */
    int getSize();

    /**
     * Returns a {@link ListIterator} that will go through all the characters in this line.
     * <br>
     * This iterator will also return any line separator present at the end of this line.
     *
     * @return a {@link ListIterator} of all the characters in this line
     */
    @Override
    ListIterator<CodeCharacter> iterator();

    /**
     * @return this entire line as a single String, including line separators
     */
    @Override
    String toString();

}
