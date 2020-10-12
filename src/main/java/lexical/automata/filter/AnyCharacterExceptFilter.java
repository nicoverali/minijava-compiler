package lexical.automata.filter;

import com.google.common.base.Joiner;
import com.google.common.primitives.Chars;
import util.Characters;

import java.util.List;
import java.util.stream.Collectors;

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
        List<String> exceptionsStr = Chars.asList(exceptions).stream()
                .map(Characters::formatSpecialCharacters).collect(Collectors.toList());
        return "not ( " + Joiner.on(" | ").join(exceptionsStr) + ")";
    }
}
