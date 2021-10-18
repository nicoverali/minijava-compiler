package util.map;

import java.util.ArrayList;
import java.util.Collection;

public class HashMultimap<K, V> extends AbstractMultimap<K, V>{

    @Override
    protected Collection<V> createCollection() {
        return new ArrayList<>();
    }

    @Override
    protected Collection<V> createCollection(Collection<? extends V> collection) {
        return new ArrayList<>(collection);
    }
}
