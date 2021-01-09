package me.whileinside.uson.cache;

import java.util.function.Supplier;

/**
 * @author Unidentified Person
 */
public class Lazy<I> implements Supplier<I> {

    private final Supplier<I> supplier;
    private I value;

    public Lazy(Supplier<I> supplier) {
        this.supplier = supplier;
    }

    public static <I> Lazy<I> of(Supplier<I> supplier) {
        return new Lazy<>(supplier);
    }

    public I get() {
        if (value == null) {
            value = supplier.get();
        }

        return value;
    }

}
