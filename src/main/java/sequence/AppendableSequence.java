package sequence;

/**
 * A collection of sequentially access elements, that can be extended with new elements.
 *
 * @param <T> type of elements in the sequence
 */
public interface AppendableSequence<T> extends Sequence<T> {

    /**
     * Appends the given element at the end of this sequence.
     * Null elements are not permitted.
     *
     * @param element element which will be appended at the end of the sequence
     * @throws IllegalArgumentException if the given <code>element</code> is null
     */
    void append(T element) throws IllegalArgumentException;

    /**
     * Appends all the given elements at the end of this sequence. Every null elements will be ignored.
     *
     * @param elements an iterable collection of elements which will be appended at the end of this sequence
     */
    void appendAll(Iterable<T> elements);

}
