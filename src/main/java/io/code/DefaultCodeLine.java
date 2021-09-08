package io.code;

import java.util.*;

class DefaultCodeLine implements CodeLine {

    private String line = "";
    private final List<CodeCharacter> characters;
    private final int lineNumber;

    /**
     * Creates a new code line with the characters from the given line, and at the given line number.
     * <br>
     * Line number should be a positive number.
     *
     * @param lineNumber position of the new code line
     * @param line characters of the new code line
     * @throws IllegalArgumentException if the line number is negative
     */
    public DefaultCodeLine(int lineNumber, String line) throws IllegalArgumentException{
        checkValidInput(lineNumber);
        this.lineNumber = lineNumber;
        this.characters = new ArrayList<>();

        for (int i = 0; i < line.length(); i++){
            this.append(line.charAt(i));
        }
    }

    private void checkValidInput(int lineNumber) throws IllegalArgumentException {
        if (lineNumber < 0)
            throw new IllegalArgumentException("Line number can't be negative");
    }

    private void append(char character){
        CodeCharacter ch = new DefaultCodeCharacter(character, characters.size(), this);
        characters.add(ch);
        line += character;
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public CodeCharacter getCharacterAt(int columnNumber) throws IndexOutOfBoundsException {
        return characters.get(columnNumber);
    }

    @Override
    public Optional<CodeCharacter> getFirstCharacter() {
        if (characters.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(characters.get(0));
    }

    @Override
    public List<CodeCharacter> getAllCharacters() {
        return Collections.unmodifiableList(characters);
    }

    @Override
    public int getSize() {
        return characters.size();
    }

    @Override
    public ListIterator<CodeCharacter> iterator() {
        return characters.listIterator();
    }

    @Override
    public String toString() {
        return line;
    }
}
