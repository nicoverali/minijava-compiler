package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Iterables {

    /**
     * Returns the first element in {@code iterable}.
     *
     * @return the first element of {@code iterable} or the default value
     * @throws NoSuchElementException if the iterable is empty
     */
    public static <T> T getFirst(Iterable<T> iterable) throws NoSuchElementException{
        return iterable.iterator().next();
    }

    /**
     * Returns the first element in {@code iterable} or {@code defaultValue} if the iterable is empty.
     *
     * @param defaultValue the default value to return if the iterable is empty
     * @return the first element of {@code iterable} or the default value
     */
    public static <T> T getFirst(Iterable<T> iterable, T defaultValue){
        Iterator<T> iterator = iterable.iterator();
        return iterator.hasNext() ? iterator.next() : defaultValue;
    }

    /**
     * Returns the last element of {@code iterable}.
     *
     * @return the last element of {@code iterable}
     * @throws NoSuchElementException if the iterable is empty
     */
    public static <T> T getLast(Iterable<T> iterable) throws NoSuchElementException {
        Iterator<T> iterator = iterable.iterator();
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return current;
            }
        }
    }

    /**
     * Returns the last element of {@code iterable} or {@code defaultValue} if the iterable is empty.
     *
     * @param defaultValue the value to return if {@code iterable} is empty
     * @return the last element of {@code iterable} or the default value
     */
    public static <T> T getLast(Iterable<T> iterable, T defaultValue){
        return iterable.iterator().hasNext() ? getLast(iterable) : defaultValue;
    }


}
