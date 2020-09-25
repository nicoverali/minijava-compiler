package io.code;

class DefaultCodeCharacter implements CodeCharacter {

    private char character;
    private int columnNumber;
    private CodeLine line;

    public DefaultCodeCharacter(char character, int columnNumber, CodeLine line) {
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

