package lexical;

public class Lexeme {

    private final StringBuilder lexemeBuilder;
    private final int lineNumber;

    /**
     * @param lineNumber lexeme starting line number
     * @return new empty lexeme
     */
    public static Lexeme empty(int lineNumber){
        return new Lexeme(lineNumber);
    }

    /**
     * Creates a new empty lexeme that starts at the given line number.
     *
     * @param lineNumber line number at which the new lexeme starts
     */
    public Lexeme(int lineNumber) {
        this.lineNumber = lineNumber;
        lexemeBuilder = new StringBuilder();
    }

    /**
     * Creates a new lexeme that starts at the given line number.
     *
     * @param lineNumber line number at which the new lexeme starts
     * @param startingLexeme the new lexeme as a String
     */
    public Lexeme(int lineNumber, String startingLexeme){
        this.lineNumber = lineNumber;
        lexemeBuilder = new StringBuilder(startingLexeme);
    }

    /**
     * Appends a new character to the current lexeme
     * @param character character to append
     */
    public void append(char character){
        lexemeBuilder.append(character);
    }

    /**
     * @return this lexeme as a String
     */
    public String getLexeme(){
        return lexemeBuilder.toString();
    }

    /**
     * @return the line number where this lexeme starts
     */
    public int getLineNumber(){
        return lineNumber;
    }

}
