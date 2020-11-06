package lexical.automata;

import io.code.CodeCharacter;
import io.code.CodeLine;
import lexical.PrependableLexeme;

import java.util.Optional;

public class AutomataLexeme implements PrependableLexeme {

    private final StringBuilder lexemeBuilder = new StringBuilder();
    private CodeLine firstLine;
    private CodeCharacter firstCharacter;

    /**
     * Creates a new Lexeme without any line or character.
     * This will represent an empty source code.
     *
     * @return a new Lexeme without line or character
     */
    public static AutomataLexeme empty(){
        return new AutomataLexeme();
    }

    /**
     * Creates a new Lexeme without a first character.
     * In this case, the Lexeme will begin at the end of the line.
     *
     * @param startingLine {@link CodeLine} where the new Lexeme begins
     * @return a new Lexeme beginning at the end of the given {@link CodeLine}
     */
    public static AutomataLexeme empty(CodeLine startingLine){
        return new AutomataLexeme(startingLine);
    }

    /**
     * Creates a new Lexeme which has a first character and first line.
     * The first line will be the line containing the given character
     *
     * @param character the first {@link CodeCharacter} of the new Lexeme
     * @return a new Lexeme with an initial first character
     */
    public static AutomataLexeme create(CodeCharacter character){
        return new AutomataLexeme(character);
    }

    private AutomataLexeme(){}

    private AutomataLexeme(CodeLine line){
        firstLine = line;
    }

    private AutomataLexeme(CodeCharacter character){
        if (character != null){
            firstLine = character.getCodeLine();
            firstCharacter = character;
            lexemeBuilder.append(character.getValue());
        }
    }

    @Override
    public boolean isEmpty() {
        return lexemeBuilder.length() == 0;
    }

    @Override
    public void prepend(CodeCharacter character) {
        lexemeBuilder.insert(0, character.getValue());
        firstCharacter = character;
        firstLine = character.getCodeLine();
    }

    @Override
    public Optional<CodeLine> getFirstLine() {
        return Optional.ofNullable(firstLine);
    }

    @Override
    public Optional<CodeCharacter> getFirstCharacter() {
        return Optional.ofNullable(firstCharacter);
    }

    @Override
    public int getLineNumber() {
        return firstLine != null
                ? firstLine.getLineNumber()
                : 0;
    }

    @Override
    public int getColumnNumber() {
        if (firstCharacter != null){
            return firstCharacter.getColumnNumber();
        } else if (firstLine != null){
            return firstLine.getSize();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return lexemeBuilder.toString();
    }
}
