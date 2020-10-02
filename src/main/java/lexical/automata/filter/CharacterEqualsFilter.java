package lexical.automata.filter;

public class CharacterEqualsFilter implements LexicalFilter{

    private final char testCharacter;

    public CharacterEqualsFilter(char testCharacter){
        this.testCharacter = testCharacter;
    }

    @Override
    public boolean test(char value) {
        return testCharacter == value;
    }

}
