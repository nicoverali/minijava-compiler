package io.code;

import java.util.*;

class DefaultCodeLine implements CodeLine {

    private static final char LINE_SEPARATOR = '\n';

    private final String lineWithoutSeparators;
    private final int lineNumber;
    private final List<CodeCharacter> characters;

    private boolean hasLineSeparator;

    /**
     * Creates a new blank code line at the given line number.
     * <br>
     * Line number should be a positive number.
     *
     * @param lineNumber position of the new code line
     * @throws IllegalArgumentException if the line number is negative
     */
    public DefaultCodeLine(int lineNumber) throws IllegalArgumentException{
        this(lineNumber, "");
    }

    /**
     * Creates a new code line with the characters from the given line, and at the given line number.
     * <br>
     * Line number should be a positive number.
     * <br>
     * The given String must not have any line separator, only common characters.
     * To add a line separator use {@link #addLineSeparator()}
     *
     * @param lineNumber position of the new code line
     * @param line characters of the new code line
     * @throws IllegalArgumentException if the line number is negative
     */
    public DefaultCodeLine(int lineNumber, String line) throws IllegalArgumentException{
        checkValidInput(lineNumber, line);
        this.lineWithoutSeparators = line;
        this.lineNumber = lineNumber;
        this.characters = new ArrayList<>();
        this.hasLineSeparator = false;

        for (int i = 0; i < line.length(); i++){
            this.append(line.charAt(i));
        }
    }

    private void checkValidInput(int lineNumber, String line) throws IllegalArgumentException {
        if (lineNumber < 0)
            throw new IllegalArgumentException("Line number can't be negative");
        if (line.contains("\n") || line.contains("\r"))
            throw new IllegalArgumentException("The given line should not have line separators");
    }

    private void append(char character){
        CodeCharacter ch = new DefaultCodeCharacter(character, characters.size(), this);
        characters.add(ch);
    }

    @Override
    public void addLineSeparator() {
        if (!hasLineSeparator){
            this.append(LINE_SEPARATOR);
            hasLineSeparator = true;
        }
    }

    @Override
    public void removeLineSeparator() {
        if (hasLineSeparator){
            characters.remove(characters.size()-1);
            hasLineSeparator = false;
        }
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
        if (hasLineSeparator){
            return lineWithoutSeparators+LINE_SEPARATOR;
        }
        return lineWithoutSeparators;
    }
}
