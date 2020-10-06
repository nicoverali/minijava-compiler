package sequence;

import java.util.Optional;

/**
 * A collection of elements that are access sequentially.
 *
 * @param <T> type of elements in the sequence
 */
public interface Sequence<T> {

    /**
     * Returns the next element in this sequence, and moves the cursor forward.
     * If there are no elements left then it returns an empty {@link Optional}
     *
     * @return an {@link Optional} wrapping the next element in the sequence
     */
    Optional<T> next();

    /**
     * @return true if there is a next element in the sequence, false otherwise
     */
    boolean hasNext();

    /**
     * Similar to {@link #next()}, but it does not move the cursor forward.
     * If there are no elements left then it returns an empty {@link Optional}
     *
     * @return an {@link Optional} wrapping the next element in the sequence
     */
    Optional<T> peek();

}
