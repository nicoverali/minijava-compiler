package util;

import java.util.*;
import java.util.stream.Collectors;

public class HashMultimap<K, V> {

    private final Map<K, Collection<V>> map = new HashMap<>();

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
    public boolean put(K key, V value){
        if (map.containsKey(key)){
            return map.get(key).add(value);
        } else {
            map.put(key, new LinkedList<>(Collections.singleton(value)));
            return true;
        }
    }

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
    public boolean putAll(K key, Collection<? extends V> values){
        if (map.containsKey(key)){
            return map.get(key).addAll(values);
        } else {
            map.put(key, new LinkedList<>(values));
            return true;
        }
    }

    /**
     * Returns a view collection of the values associated with {@code key} in this multimap, if any.
     * Note that when {@code containsKey(key)} is false, this returns an empty collection, not {@code
     * null}.
     *
     * <p>Changes to the returned collection will update the underlying multimap, and vice versa.
     */
    public Collection<V> get(K key){
        return map.get(key);
    }

    /**
     * Returns {@code true} if this multimap contains at least one key-value pair with the key {@code
     * key}.
     */
    public boolean containsKey(K key){
       return map.containsKey(key);
    }

    /**
     * Returns a view collection of all <i>distinct</i> keys contained in this multimap. Note that the
     * key set contains a key if and only if this multimap maps that key to at least one value.
     *
     * <p>Changes to the returned set will update the underlying multimap, and vice versa. However,
     * <i>adding</i> to the returned set is not possible.
     */
    public Set<K> keys(){
        return map.keySet();
    }

    /**
     * Returns a view collection containing the <i>value</i> from each key-value pair contained in
     * this multimap, without collapsing duplicates (so {@code values().size() == size()}).
     *
     * <p>Changes to the returned collection will update the underlying multimap, and vice versa.
     * However, <i>adding</i> to the returned collection is not possible.
     */
    public Collection<V> values(){
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * Removes all of the mappings from this map (optional operation). The map will be empty after this call returns
     */
    public void clear(){
        map.clear();
    }

}
