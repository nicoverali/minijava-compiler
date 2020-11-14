package lexical.automata.filter;

import java.util.Arrays;

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

    @Override
    public String toString() {
//        List<String> exceptionsStr = Chars.asList(exceptions).stream()
//                .map(Characters::explicitSpecialChars).collect(Collectors.toList());
//        return "not ( " + Joiner.on(" | ").join(exceptionsStr) + ")";
        return "not ("+ Arrays.toString(exceptions)+")";
    }
}
