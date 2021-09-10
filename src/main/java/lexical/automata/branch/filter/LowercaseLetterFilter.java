package lexical.automata.branch.filter;

public class LowercaseLetterFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isLowerCase(value);
    }

    @Override
    public String toString() {
        return "is a lowercase letter";
    }
}
