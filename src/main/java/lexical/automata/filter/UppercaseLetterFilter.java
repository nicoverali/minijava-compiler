package lexical.automata.filter;

public class UppercaseLetterFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isUpperCase(value);
    }
}
