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

import me.whileinside.uson.util.CharArraySequence;

import java.util.Arrays;

/**
 * @author Unidentified Person
 */
public final class Indent {

    private final IndentType _type;

    private final int _size;
    private final CharArraySequence _value;

    public Indent(IndentType type, int size) {
        _type = type;
        _size = size;

        char[] value = new char[type.getLength() * size];
        Arrays.fill(value, type.getValue());

        this._value = new CharArraySequence(value);
    }

    public IndentType getType() {
        return _type;
    }

    public int getSize() {
        return _size;
    }

    @Override
    public String toString() {
        return "{" + _size + "x" + _type + "}";
    }

    public CharSequence getValue() {
        return _value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Indent)) return false;

        Indent another = (Indent) o;

        return _type == another._type
                && _size == another._size;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + _type.hashCode();
        result = result * 31 + _size;

        return result;
    }

}
