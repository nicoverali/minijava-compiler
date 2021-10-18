package util.map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Multimap<K, V> {

    /**
     * Stores a key-value pair in this multimap.
     *
     * <p>Some multimap implementations allow duplicate key-value pairs, in which case {@code put}
     * always adds a new key-value pair and increases the multimap size by 1. Other implementations
     * prohibit duplicates, and storing a key-value pair that's already in the multimap has no effect.
     *
     * @return {@code true} if the method increased the size of the multimap, or {@code false} if the
     *     multimap already contained the key-value pair and doesn't allow duplicates
     */
    boolean put(K key, V value);

    /**
     * Stores a key-value pair in this multimap for each of {@code values}, all using the same key,
     * {@code key}. Equivalent to (but expected to be more efficient than):
     *
     * <pre>{@code
     * for (V value : values) {
     *   put(key, value);
     * }
     * }</pre>
     *
     * <p>In particular, this is a no-op if {@code values} is empty.
     *
     * @return {@code true} if the multimap changed
     */
    boolean putAll(K key, Collection<? extends V> values);

    /**
     * Copies all mappings from the specified map to this multimap (optional operation).
     * The effect of this call is equivalent to that of calling put(k, v) on this map once for each mapping from key k to value v in the specified map.
     *
     * The behavior of this operation is undefined if the specified map is modified while the operation is in progress.
     *
     * @return {@code true} if the multimap changed
     */
    boolean putAll(Multimap<K, V> multimap);

    /**
     * Returns a view collection of the values associated with {@code key} in this multimap, if any.
     * Note that when {@code containsKey(key)} is false, this returns an empty collection, not {@code
     * null}.
     *
     * <p>Changes to the returned collection will update the underlying multimap, and vice versa.
     */
    Collection<V> get(K key);

    /**
     * Returns {@code true} if this multimap contains at least one key-value pair with the key {@code
     * key}.
     */
    boolean containsKey(K key);

    /**
     * Returns a view collection of all <i>distinct</i> keys contained in this multimap. Note that the
     * key set contains a key if and only if this multimap maps that key to at least one value.
     *
     * <p>Changes to the returned set will update the underlying multimap, and vice versa. However,
     * <i>adding</i> to the returned set is not possible.
     */
    Set<K> keys();

    /**
     * Returns a view collection containing the <i>value</i> from each key-value pair contained in
     * this multimap, without collapsing duplicates (so {@code values().size() == size()}).
     *
     * <p>Changes to the returned collection will update the underlying multimap, and vice versa.
     * However, <i>adding</i> to the returned collection is not possible.
     */
    Collection<V> values();

    /**
     * Removes all of the mappings from this map (optional operation). The map will be empty after this call returns
     */
    void clear();


}
