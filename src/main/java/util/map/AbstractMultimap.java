package util.map;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractMultimap<K, V> implements Multimap<K, V>{


    private final Map<K, Collection<V>> map = new HashMap<>();

    abstract protected Collection<V> createCollection();

    abstract protected Collection<V> createCollection(Collection<? extends V> collection);

    public AbstractMultimap() {
    }

    public AbstractMultimap(Multimap<K, V> multimap){
        this.putAll(multimap);
    }

    @Override
    public boolean put(K key, V value){
        if (map.containsKey(key)){
            return map.get(key).add(value);
        } else {
            map.put(key, createCollection(Collections.singleton(value)));
            return true;
        }
    }

    @Override
    public boolean putAll(K key, Collection<? extends V> values){
        if (map.containsKey(key)){
            return map.get(key).addAll(values);
        } else {
            map.put(key, createCollection(values));
            return true;
        }
    }

    @Override
    public boolean putAll(Multimap<K, V> multimap){
        boolean changed = false;
        for (K key : multimap.keys()) {
            if (this.map.containsKey(key)) {
                changed |= this.map.get(key).addAll(multimap.get(key));
            } else {
                this.map.put(key, createCollection(multimap.get(key)));
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public Collection<V> get(K key){
        Collection<V> values = map.get(key);

        return values != null
                ? values
                : List.of();
    }

    @Override
    public boolean containsKey(K key){
        return map.containsKey(key);
    }

    @Override
    public Set<K> keys(){
        return map.keySet();
    }

    @Override
    public Collection<V> values(){
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public void clear(){
        map.clear();
    }


}
