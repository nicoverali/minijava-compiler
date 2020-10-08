package lexical.automata.filter;

public class CharacterNotEqualsFilter implements LexicalFilter{

    private final char testCharacter;

    public CharacterNotEqualsFilter(char testCharacter){
        this.testCharacter = testCharacter;
    }

    @Override
    public boolean test(char value) {
        return testCharacter != value;
    }

}
