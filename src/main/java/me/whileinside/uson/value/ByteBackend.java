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

package me.whileinside.uson.value;

import java.math.BigDecimal;

public final class ByteBackend extends ValueBackend {

    private final byte value;

    public ByteBackend(byte value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ValueBackend)) return false;

        if (obj instanceof ByteBackend) {
            ByteBackend that = (ByteBackend) obj;

            return value == that.value;
        }

        return super.equals(obj);
    }

    @Override
    public String getString() {
        return Byte.toString(value);
    }

    @Override
    public byte getByte() {
        return value;
    }

    @Override
    public short getShort() {
        return value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public float getFloat() {
        return value;
    }

    @Override
    public boolean getBoolean() {
        return false;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return new BigDecimal(value);
    }
}