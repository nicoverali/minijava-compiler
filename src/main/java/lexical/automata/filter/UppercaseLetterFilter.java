package lexical.automata.filter;

public class UppercaseLetterFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isUpperCase(value);
    }

    @Override
    public String toString() {
        return "is an uppercase letter";
    }
}
