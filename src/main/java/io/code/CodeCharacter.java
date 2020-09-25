package io.code;

public interface CodeCharacter {

    /**
     * @return this character as a <i>char</i>
     */
    char getValue();

    /**
     * @return the line were this <i>CodeCharacter</i> belongs
     */
    CodeLine getCodeLine();

    /**
     * Return the number of line where this <i>CodeCharacter</i> belongs. Line numbers
     * start from row 1
     *
     * @return the number of line were this <i>CodeCharacter</i> belongs
     */
    int getLineNumber();

    /**
     * Returns the position (column) of this <i>CodeCharacter</i>. The position of
     * a character starts from column 1
     *
     * @return the position of this <i>CodeCharacter</i> inside the line were it belongs
     */
    int getColumnNumber();

    /**
     * Checks if the value of this <i>CodeCharacter</i> equals the character given as argument.
     *
     * @param character a <i>char</i> value to compare with
     * @return true if this <i>CodeCharacter</i> value equals the one given as argument
     */
    boolean equals(char character);

}
