package ru.hse.java.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.function.Predicate;

public abstract class UnmodifiableCollection<E> extends AbstractCollection<E> {
    protected static final String CANNOT_MODIFY_MSG = "Can't modify this collection.";

    @Override
    public boolean add(E e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CANNOT_MODIFY_MSG);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CANNOT_MODIFY_MSG);
    }

    @Override
    public void clear() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CANNOT_MODIFY_MSG);
    }

    @Override
    public boolean remove(Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CANNOT_MODIFY_MSG);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CANNOT_MODIFY_MSG);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CANNOT_MODIFY_MSG);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(CANNOT_MODIFY_MSG);
    }
}
