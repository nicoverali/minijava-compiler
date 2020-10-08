package lexical.automata.filter;

public class WhitespaceFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isWhitespace(value);
    }
}
