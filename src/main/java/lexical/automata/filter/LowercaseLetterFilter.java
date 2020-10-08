package lexical.automata.filter;

public class LowercaseLetterFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isLowerCase(value);
    }
}
