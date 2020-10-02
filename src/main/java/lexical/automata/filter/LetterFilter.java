package lexical.automata.filter;

public class LetterFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isLetter(value);
    }
}
