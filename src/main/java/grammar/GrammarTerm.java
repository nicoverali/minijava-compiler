package grammar;

import java.util.Objects;

public class GrammarTerm {

    private final String name;
    private final boolean isNonTerminal;
    private int originalLine = -1;

    public GrammarTerm(String name, boolean isNonTerminal) {
        this.name = name;
        this.isNonTerminal = isNonTerminal;
    }

    public GrammarTerm(String name, boolean isNonTerminal, int originalLine) {
        this.name = name;
        this.isNonTerminal = isNonTerminal;
        this.originalLine = originalLine;
    }

    /**
     * Returns the original line number where this term was located in the grammar source file.
     * If this term was not originated from a source file the its line number will be equal to -1.
     *
     * @return the original line number of this term, or -1 if this term was not originated from a source file.
     */
    public int getLineNumber(){
        return originalLine;
    }

    /**
     * @return true if this grammar term is a non terminal, false if it is a terminal
     */
    public boolean isNonTerminal(){
        return isNonTerminal;
    }

    /**
     * @return the name of this grammar term
     */
    public String getName(){
        return name;
    }

    /**
     * Checks whether this term is equal to another {@link GrammarTerm} or a {@link String}.
     * <li>Two {@link GrammarTerm} will be equal if both are terminal or non terminal, and their name is the same.</li>
     * <li>A term will be equal to a String if the name of the term is equal to the String.</li>
     * If the given object is not of one of those types then it returns false.
     *
     * @param other another object to compare with
     * @return true if the <code>other</code> is equal to this term, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof GrammarTerm){
            GrammarTerm otherTerm = (GrammarTerm) other;
            return otherTerm.isNonTerminal == this.isNonTerminal && otherTerm.name.equals(name);
        } else if (other instanceof String){
            return name.equals(other);
        }
        return false;
    }

    /**
     * @return the hashcode of this term name
     */
    @Override
    public int hashCode(){
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
