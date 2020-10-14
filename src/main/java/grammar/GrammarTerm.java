package grammar;

import java.util.Objects;

public class GrammarTerm {

    private final String term;

    public GrammarTerm(String term) {
        this.term = term;
    }

    public boolean isNonTerminal(){
        return term.charAt(0) == '<' && term.charAt(term.length()-1) == '>';
    }

    public boolean equals(Object other){
        if (other instanceof GrammarTerm){
            return term.equals(other.toString());
        } else if (other instanceof String){
            return term.equals(other);
        }
        return false;
    }

    public boolean equals(GrammarTerm other){
        return term.equals(other.term);
    }

    public boolean equals(String other){
        return term.equals(other);
    }

    public String toString(){
        return term;
    }

    @Override
    public int hashCode() {
        return Objects.hash(term);
    }
}
