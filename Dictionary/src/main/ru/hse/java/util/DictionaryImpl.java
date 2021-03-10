package ru.hse.java.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DictionaryImpl<K, V> extends AbstractMap<K, V> implements Dictionary<K, V> {
    private ArrayList<HashMapElement> hashMap;
    private int size = 0, sizeWithDeleted = 0;
    private final double loadFactor;

    private static final int INITIAL_CAPACITY = 16;
    private static final double INITIAL_LOAD_FACTOR = 0.75;
    private static final int CAPACITY_CHANGE_FACTOR = 2;
    private static final double MINIMAL_LOAD_FACTOR = 0.25;

    private class HashMapElement extends SimpleEntry<K, V> {
        private boolean isDeleted = false;

        public HashMapElement(Entry<? extends K, ? extends V> entry) {
            super(entry);
        }

        public HashMapElement(K key, V value) {
            this(new SimpleEntry<>(key, value));
        }

        public HashMapElement(K key, V value, boolean isDeleted) {
            this(key, value);
            this.isDeleted = isDeleted;
        }

    }

    public DictionaryImpl() {
        this(INITIAL_CAPACITY, INITIAL_LOAD_FACTOR);
    }

    public DictionaryImpl(double loadFactor) {
        this(INITIAL_CAPACITY, loadFactor);
    }

    public DictionaryImpl(int capacity, double loadFactor) {
        this.loadFactor = loadFactor;
        createLists(capacity);
    }

    private void createLists(int capacity) {
        hashMap = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            hashMap.add(null);
        }
    }

    @Override
    public int size() {
        return size;
    }

    private int getIndexFromKey(Object key) {
        int index = Math.abs(key.hashCode()) % hashMap.size();
        HashMapElement curElement = hashMap.get(index);
        while (!(curElement == null || (key.equals(curElement.getKey()) && !curElement.isDeleted))) {
            index = (index + 1) % hashMap.size();
            curElement = hashMap.get(index);
        }
        return index;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = getIndexFromKey(key);
        HashMapElement curElement = hashMap.get(index);
        return curElement != null;
    }

    @Override
    @Nullable
    public V get(Object key) {
        if (!containsKey(key)) {
            return null;
        }
        int index = getIndexFromKey(key);
        return hashMap.get(index).getValue();
    }

    private void rebuild() {
        double curLoadFactor = (double)sizeWithDeleted / (double)hashMap.size();
        if (curLoadFactor <= loadFactor && (curLoadFactor >= MINIMAL_LOAD_FACTOR * loadFactor || hashMap.size() == INITIAL_CAPACITY)) {
            return;
        }
        ArrayList<HashMapElement> previousHashMap = hashMap;
        if (curLoadFactor > loadFactor) {
            createLists(previousHashMap.size() * CAPACITY_CHANGE_FACTOR);
        } else {
            createLists(previousHashMap.size() / CAPACITY_CHANGE_FACTOR);
        }
        size = 0;
        for (HashMapElement curElement : previousHashMap) {
            if (curElement != null && !curElement.isDeleted) {
                putWithoutRebuild(curElement.getKey(), curElement.getValue());
            }
        }
        sizeWithDeleted = size;
    }

    @Nullable
    private V putWithoutRebuild(K key, V value) {
        V result = get(key);
        int index = getIndexFromKey(key);
        HashMapElement curEntry = new HashMapElement(key, value);
        hashMap.set(index, curEntry);
        if (result == null) {
            ++size;
        }
        return result;
    }

    @Override
    @Nullable
    public V put(K key, V value) {
        V result = putWithoutRebuild(key, value);
        if (result == null) {
            ++sizeWithDeleted;
        }
        rebuild();
        return result;
    }

    @Nullable
    private V removeWithoutRebuild(Object key) {
        V result = get(key);
        int index = getIndexFromKey(key);
        HashMapElement curElement = hashMap.get(index);
        if (result != null) {
            hashMap.set(index, new HashMapElement(curElement.getKey(), curElement.getValue(), true));
            --size;
        }
        return result;
    }

    @Override
    public V remove(Object key) {
        V result = removeWithoutRebuild(key);
        rebuild();
        return result;
    }

    @Override
    public void clear() {
        size = sizeWithDeleted = 0;
        createLists(INITIAL_CAPACITY);
    }

    private abstract class DictionaryHelperIterator<E> implements Iterator<E> {
        protected int index = 0; //points to the iterator.next()
        protected int elementIndexForRemove = -1;

        protected boolean canRemove() {
            return elementIndexForRemove != -1;
        }

        @Override
        public boolean hasNext() {
            while (index < hashMap.size() && (hashMap.get(index) == null || hashMap.get(index).isDeleted)) {
                ++index;
            }
            return index < hashMap.size();
        }

        @Override
        public void remove() throws IllegalStateException {
            if (!canRemove()) {
                throw new IllegalStateException("There was no next() before remove().");
            }
            removeWithoutRebuild(hashMap.get(elementIndexForRemove).getKey());
            elementIndexForRemove = -1;
        }
    }

    private class KeySetIterator extends DictionaryHelperIterator<K> {
        @Override
        public K next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("Key set has no more elements.");
            }
            elementIndexForRemove = index;
            ++index;
            HashMapElement curElement = hashMap.get(index - 1);
            return curElement.getKey();
        }
    }

    private class KeySet extends UnmodifiableSet<K> {
        @Override
        @NotNull
        public Iterator<K> iterator() {
            return new KeySetIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            return containsKey(o);
        }
    }

    private class ValueCollectionIterator extends DictionaryHelperIterator<V> {
        @Override
        public V next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("Value collection has no more elements.");
            }
            elementIndexForRemove = index;
            ++index;
            HashMapElement curElement = hashMap.get(index - 1);
            return curElement.getValue();
        }
    }

    private class ValueCollection extends UnmodifiableCollection<V> {
        @Override
        @NotNull
        public Iterator<V> iterator() {
            return new ValueCollectionIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    private class EntrySetIterator extends DictionaryHelperIterator<Entry<K, V>> {
        @Override
        public Entry<K, V> next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("Entry set has no more elements.");
            }
            elementIndexForRemove = index;
            ++index;
            HashMapElement curElement = hashMap.get(index - 1);
            return new SimpleEntry<>(curElement.getKey(), curElement.getValue());
        }
    }

    private class EntrySet extends UnmodifiableSet<Entry<K, V>> {
        @Override
        @NotNull
        public Iterator<Entry<K, V>> iterator() {
            return new EntrySetIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> oEntry = (Map.Entry<?, ?>) o;
                int index = getIndexFromKey(oEntry.getKey());
                return hashMap.get(index).getValue().equals(oEntry.getValue());
            }
            return false;
        }
    }

    @Override
    public @NotNull Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        return new ValueCollection();
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }
}
