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

package me.whileinside.uson.indent;

/**
 * @author Unidentified Person
 */
public final class Indents {

    private static final Indent[][] CACHE = new Indent[IndentType.COUNT][];

    private Indents() {
        throw new UnsupportedOperationException();
    }

    public static CharSequence getIndentString(IndentType type, int size) {
        return getIndent(type, size).getValue();
    }

    public static synchronized Indent getIndent(IndentType type, int size) {
        if (size < 0) throw new IllegalArgumentException("size must be greater than or equal to zero");

        Indent[] cache = CACHE[type.idx];

        if (cache == null) {
            CACHE[type.idx] = cache = new Indent[4];
        }

        if (cache.length <= size) {
            Indent[] indentCache = new Indent[Math.max(cache.length << 1, size)];
            System.arraycopy(cache, 0, indentCache, 0, cache.length);

            CACHE[type.idx] = cache = indentCache;
        }

        Indent indent = cache[size];

        if (indent == null) {
            cache[size] = indent = new Indent(type, size);
        }

        return indent;
    }


}
