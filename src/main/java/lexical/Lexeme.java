package lexical;

public class Lexeme {

    private final StringBuilder lexemeBuilder;
    private final int lineNumber;

    /**
     * Creates a new empty lexeme that starts at the given line number.
     *
     * @param lineNumber line number at which the new lexeme starts
     * @return new empty lexeme
     */
    public static Lexeme empty(int lineNumber){
        return new Lexeme(lineNumber);
    }

    /**
     * @param lexeme a {@link Lexeme} which state will be copied
     * @return a new lexeme with the same state as the one given
     */
    public static Lexeme from(Lexeme lexeme){
        return new Lexeme(lexeme.toString(), lexeme.getLineNumber());
    }

    /**
     * Creates a new lexeme that starts at the given line number.
     *
     * @param lexeme the new lexeme as a String
     * @param lineNumber line number at which the new lexeme starts
     */
    public static Lexeme create(String lexeme, int lineNumber){
        return new Lexeme(lexeme, lineNumber);
    }


    private Lexeme(int lineNumber) {
        this.lineNumber = lineNumber;
        lexemeBuilder = new StringBuilder();
    }


    private Lexeme(String startingLexeme, int lineNumber){
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
     * Prepends a new character to the current lexeme
     *
     * @param character character to prepend
     */
    public void prepend(char character) {
        lexemeBuilder.insert(0, character);
    }


    /**
     * @return the line number where this lexeme starts
     */
    public int getLineNumber(){
        return lineNumber;
    }

    /**
     * @return this lexeme as a String
     */
    @Override
    public String toString(){
        return lexemeBuilder.toString();
    }
}
