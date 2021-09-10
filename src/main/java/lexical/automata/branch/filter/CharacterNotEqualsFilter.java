package lexical.automata.branch.filter;

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
        return "not equals " + Characters.explicitSpecialChars(testCharacter);
    }
}
