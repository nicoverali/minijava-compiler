package io.code;

public interface CodeCharacter {

    /**
     * @return this character as a <code>char</code>
     */
    char getValue();

    /**
     * @return the line were this <code>CodeCharacter</code> belongs
     */
    CodeLine getCodeLine();

    /**
     * Return the line number of this <code>CodeCharacter</code>. Line numbers
     * are zero-based indexed.
     *
     * @return the number of line were this <code>CodeCharacter</code> belongs
     */
    int getLineNumber();

    /**
     * Returns the position (column) of this <code>CodeCharacter</code> within a line. The position of
     * a character are zero-based indexed.
     *
     * @see #getCodeLine()
     * @return the position of this <code>CodeCharacter</code> inside the line were it belongs
     */
    int getColumnNumber();

    /**
     * Checks if the value of this <code>CodeCharacter</code> equals the character given as argument.
     *
     * @param character a <code>char</code> value to compare with
     * @return true if this <code>CodeCharacter</code> value equals the one given as argument
     */
    boolean equals(char character);

}
