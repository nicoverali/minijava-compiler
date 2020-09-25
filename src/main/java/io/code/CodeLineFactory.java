package io.code;

/**
 * This factory will generate {@link CodeLine}s with a line number that increses on every generation.
 */
public interface CodeLineFactory {

    /**
     * Creates a new {@link CodeLine} with the string given as argument. The line number will increase by one.
     *
     * @return a new line with the string given as argument and a new line number
     */
    CodeLine create(String line);

}
