package lexical.automata.filter;

import util.Characters;

public class DigitFilter implements LexicalFilter{
    @Override
    public boolean test(char value) {
        return Character.isDigit(value);
    }

    @Override
    public String toString() {
        return "is a digit";
    }
}
