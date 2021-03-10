package ru.hse.java.util;

import java.util.Collection;
import java.util.Set;

public abstract class UnmodifiableSet<E> extends UnmodifiableCollection<E> implements Set<E> {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Set) {
            Collection<?> objSet = (Collection<?>) obj;
            if (objSet.size() == size()) {
                return containsAll(objSet);
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (E element : this) {
            if (element != null) {
                result += element.hashCode();
            }
        }
        return result;
    }
}
