package io.code;


class DefaultCodeCharacter implements CodeCharacter {

    private final char character;
    private final int columnNumber;
    private final CodeLine line;

    /**
     * Creates a new <code>CodeCharacter</code> with the value given as argument.
     * <br>
     * Column number must be a positive number.
     *
     * @param character value of this <code>CodeCharacter</code>
     * @param columnNumber the position of this character in a line
     * @param line the {@link CodeLine} where this <code>CodeCharacter</code> belongs
     * @throws IllegalArgumentException if the column number is negative or the line is null
     */
    public DefaultCodeCharacter(char character, int columnNumber, CodeLine line) throws IllegalArgumentException{
        if (columnNumber < 0) throw new IllegalArgumentException("The column number of a CodeCharacter can't be negative");
        this.character = character;
        this.line = line;
        this.columnNumber = columnNumber;
    }

    @Override
    public char getValue() {
        return character;
    }

    @Override
    public CodeLine getCodeLine() {
        return line;
    }

    @Override
    public int getLineNumber() {
        return line.getLineNumber();
    }

    @Override
    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public boolean equals(char character) {
        return this.character == character;
    }

    @Override
    public String toString() {
        return "DefaultCodeCharacter{" +
                "character=" + character +
                ", line=" + line.getLineNumber() +
                ", column=" + columnNumber +
                '}';
    }
}

