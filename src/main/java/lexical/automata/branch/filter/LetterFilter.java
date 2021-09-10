package lexical.automata.branch.filter;

public class LetterFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isLetter(value);
    }

    @Override
    public String toString() {
        return "is a letter";
    }
}
