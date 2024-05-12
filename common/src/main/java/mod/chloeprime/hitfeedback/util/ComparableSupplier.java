package mod.chloeprime.hitfeedback.util;

import java.util.Objects;
import java.util.function.Supplier;

public final class ComparableSupplier<T> implements Supplier<T> {

    private final Supplier<T> delegate;

    public ComparableSupplier(Supplier<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T get() {
        return delegate.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparableSupplier<?> that = (ComparableSupplier<?>) o;
        return Objects.equals(delegate.get(), that.delegate.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(delegate.get());
    }
}
