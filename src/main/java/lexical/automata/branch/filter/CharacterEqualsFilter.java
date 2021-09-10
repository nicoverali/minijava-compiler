package lexical.automata.branch.filter;

import util.Characters;

public class CharacterEqualsFilter implements LexicalFilter{

    private final char testCharacter;

    public CharacterEqualsFilter(char testCharacter){
        this.testCharacter = testCharacter;
    }

    @Override
    public boolean test(char value) {
        return testCharacter == value;
    }

    @Override
    public String toString() {
        return "equals " + Characters.explicitSpecialChars(testCharacter);
    }
}
