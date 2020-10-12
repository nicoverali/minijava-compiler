package lexical.automata.filter;

import util.Characters;

public class CharacterNotEqualsFilter implements LexicalFilter{

    private final char testCharacter;

    public CharacterNotEqualsFilter(char testCharacter){
        this.testCharacter = testCharacter;
    }

    @Override
    public boolean test(char value) {
        return testCharacter != value;
    }

    @Override
    public String toString() {
        return "not equals " + Characters.formatSpecialCharacters(testCharacter);
    }
}
