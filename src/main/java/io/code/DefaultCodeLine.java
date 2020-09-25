package io.code;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DefaultCodeLine implements CodeLine {

    private int lineNumber;
    private List<CodeCharacter> characters;

    public DefaultCodeLine(int lineNumber) {
        this.lineNumber = lineNumber;
        characters = new ArrayList<>();
    }

    public DefaultCodeLine(int lineNumber, String line){
        this.lineNumber = lineNumber;
        this.characters = new ArrayList<>();

        for (int i = 0; i < line.length(); i++){
            this.append(line.charAt(i));
        }
    }

    public void append(char character){
        CodeCharacter ch = new DefaultCodeCharacter(character, characters.size()+1, this);
        characters.add(ch);
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
    public List<CodeCharacter> getAllCharacters() {
        return characters;
    }

    @Override
    public int getSize() {
        return getLineWithoutTerminators().length();
    }

    @Override
    public String toString(){
        return getLineWithoutTerminators();
    }

    @Override
    public Iterator<CodeCharacter> iterator() {
        return characters.iterator();
    }

    private String getLineWithoutTerminators(){
        StringBuilder line = new StringBuilder();
        characters.forEach(ch -> line.append(ch.getValue()));
        return line.toString().replaceAll("[\n\r]", "");
    }
}
