package lexical.automata.filter;

public class WhitespaceFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isWhitespace(value);
    }

    @Override
    public String toString() {
        return "is whitespace";
    }
}
