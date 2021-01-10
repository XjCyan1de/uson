/*
 *    Copyright 2021 Unidentified Person
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.whileinside.uson.util;

import java.util.function.Supplier;

/**
 * @author Unidentified Person
 */
public final class Lazy<I> implements Supplier<I> {

    private final Supplier<I> supplier;
    private I value;

    private Lazy(Supplier<I> supplier) {
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
