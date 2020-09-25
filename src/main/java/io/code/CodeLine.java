package io.code;

import java.util.Iterator;
import java.util.List;

public interface CodeLine extends Iterable<CodeCharacter> {

    /**
     * Returns the position (row) of this line inside the file. Line numbers start from row 1
     *
     * @return the position of this line inside the file where it belongs
     */
    int getLineNumber();

    /**
     * Returns a list of all the characters. This will also include any line terminator that may
     * be present in this line.
     *
     * @return a list of all the characters within this line.
     */
    List<CodeCharacter> getAllCharacters();

    /**
     * Returns the character which position in this line is the same as the one given as argument.
     *
     * @param column character's position
     * @return the character in this line with the given position
     * @throws IndexOutOfBoundsException if the given column number exceeds the size of this line
     */
    CodeCharacter getCharacterAt(int column) throws IndexOutOfBoundsException;

    /**
     * @return the number of characters in this line, excluding any line terminator
     */
    int getSize();

    /**
     * Returns an iterator that will go through all the characters in this line. This iterator will
     * also return any line terminator that may be present.
     *
     * @return an iterator of all the characters in this line
     */
    @Override
    Iterator<CodeCharacter> iterator();

    /**
     * @return this entire line as a single String, excluding any line terminator
     */
    @Override
    String toString();

}
