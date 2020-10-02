package lexical.automata.filter;

public class AnyCharacterExceptFilter implements LexicalFilter{

    private final char[] exceptions;

    public AnyCharacterExceptFilter(char... exceptions){
        this.exceptions = exceptions;
    }

    @Override
    public boolean test(char value) {
        for (char exception : exceptions){
            if (exception == value){
                return false;
            }
        }
        return true;
    }
}
