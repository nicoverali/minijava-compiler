package grammar.generator;

import com.google.common.base.VerifyException;
import grammar.io.GrammarValidator;

import java.util.Collection;

/**
 * This grammar validator will verify that terminal terms are the ones we have in MiniJava.
 */
public class GeneratorGrammarValidator implements GrammarValidator {

    @Override
    public void validateNonTerminal(String term, Collection<String> heads, int lineNumber){
        if (!heads.contains(term)){
            throw new IllegalArgumentException("Non terminal "+term+" at line "+lineNumber+" does not appear on left-side");
        }
    }

    @Override
    public String validateTerminal(String term, int lineNumber){
        try {
            return GrammarTokenMapper.map(term).name();
        } catch (VerifyException e){
            throw new IllegalArgumentException("Terminal "+term+" at line "+lineNumber+" is not a valid Token", e);
        }
    }

}
