package util.map;

import java.util.*;
import java.util.stream.Collectors;

public class HashSetMultimap<K, V> extends AbstractMultimap<K, V>{

    public HashSetMultimap() {
    }

    public HashSetMultimap(Multimap<K, V> multimap) {
        super(multimap);
    }

    @Override
    protected Collection<V> createCollection() {
        return new HashSet<>();
    }

    @Override
    protected Collection<V> createCollection(Collection<? extends V> collection) {
        return new HashSet<>(collection);
    }
}
