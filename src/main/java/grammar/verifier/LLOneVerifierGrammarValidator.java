package grammar.verifier;

import grammar.io.GrammarValidator;

import java.util.Collection;

public class LLOneVerifierGrammarValidator implements GrammarValidator {

    @Override
    public void validateNonTerminal(String term, Collection<String> heads, int lineNumber){
        if (!heads.contains(term)){
            throw new IllegalArgumentException("Non terminal "+term+" at line "+lineNumber+" does not appear on left-side");
        }
    }

    @Override
    public String validateTerminal(String term, int lineNumber){
        return term;
    }

}
