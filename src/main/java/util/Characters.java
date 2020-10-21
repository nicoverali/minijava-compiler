package util;

public class Characters {

    private Characters(){}

    /**
     * If the given character is a special slash character, then it returns a literal String representation.
     * <br>
     * For example:
     * <blockquote>
     *     <code>
     *     '\n' => "\\n"
     *     <br>
     *     '\t' => "\\t"
     *     </code>
     * </blockquote>
     * In case the given character is not a special character, then the method
     * will simply return the character as a String.
     *
     * @param character a char value which will be formatted to a literal String representation.
     * @return a literal String representation of the given <code>character</code>
     */
    public static String explicitSpecialChars(char character){
        switch (character){
            case '\n': return "\\n";
            case '\t': return "\\t";
            case '\r': return "\\r";
            default:   return String.valueOf(character);
        }
    }

    /**
     * Replaces every special slash character of the given String with its literal String representation.
     * <br>
     * For example:
     * <blockquote>
     *     <code>
     *     "Hello\n" => "Hello\\n"
     *     <br>
     *     'World\t' => "World\\t"
     *     </code>
     * </blockquote>
     *
     * @see #explicitSpecialChars(char)
     * @param text a String which all special characters will be formatted to its literal String representation.
     * @return a literal String representation of the given <code>character</code>
     */
    public static String explicitSpecialChars(String text){
        return text.replaceAll("\n", "\\n")
                .replaceAll("\r", "\\r")
                .replaceAll("\t", "\\t");
    }

    /**
     * Removes all new line characters from the given text
     *
     * @param text a text from which new line characters will be removed
     * @return the given <code>text</code> without new line characters
     */
    public static String removeNewLine(String text){
        return text.replaceAll("[\n\r]", "");
    }


}
