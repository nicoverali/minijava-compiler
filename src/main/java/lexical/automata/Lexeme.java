package lexical.automata;

import io.code.CodeCharacter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Lexeme {

    private final List<CodeCharacter> lexeme;
    private final StringBuilder lexemeString = new StringBuilder();

    public static Lexeme empty(){
        return new Lexeme();
    }

    public static Lexeme from(List<CodeCharacter> characters) {
        return new Lexeme(characters);
    }

    private Lexeme() {
        this.lexeme = new ArrayList<>();
    }

    private Lexeme(List<CodeCharacter> characters) {
        this.lexeme = characters;
        characters.forEach(ch -> lexemeString.append(ch.getValue()));
    }

    public void add(CodeCharacter character) {
        lexeme.add(character);
        lexemeString.append(character.getValue());
    }

    public Optional<CodeCharacter> getFirst(){
        if (lexeme.isEmpty()) return Optional.empty();
        return Optional.of(lexeme.get(0));
    }

    public List<CodeCharacter> getAllCharacters(){
        return Collections.unmodifiableList(lexeme);
    }

    @Override
    public String toString() {
        return lexemeString.toString();
    }
}
