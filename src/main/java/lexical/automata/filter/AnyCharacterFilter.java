package lexical.automata.filter;

public class AnyCharacterFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }
}
