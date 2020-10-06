package sequence;

/**
 * All Markable represent a sequence of elements that can be marked. The sequence then can be reset thus moving
 * the cursor back to the last marker.
 *
 * @param <T> type of elements in the sequence
 */
public interface MarkableSequence<T> extends Sequence<T> {

    /**
     * Marks the sequence at the current cursor position. The marker will remain until the cursor moves beyond
     * the <code>readAheadLimit</code>. Once this limit is reached, the marker may be deleted at any time, so
     * calling {@link #reset()} at this point is uncertain.
     * <br>
     * You can flush all expired markers with {@link #flushMarkers()}
     * <br><br>
     * The limit for the marker must be positive.
     *
     * @param readAheadLimit number of positions the cursor may move before deleting the marker
     * @throws IllegalArgumentException if <code>readAheadLimit</code> is negative
     */
    void mark(int readAheadLimit) throws IllegalArgumentException;

    /**
     * Deletes all expired markers.
     */
    void flushMarkers();

    /**
     * Resets the sequence cursor to the last marker, and deletes the marker. If no marker exists, then this method
     * will throw an exception.
     *
     * @throws IllegalStateException if there are no markers
     */
    void reset() throws IllegalStateException;
}
