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

public final class FloatBackend extends ValueBackend {

    private final float value;

    public FloatBackend(float value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ValueBackend)) return false;

        if (obj instanceof FloatBackend) {
            FloatBackend that = (FloatBackend) obj;

            return value == that.value;
        }

        return super.equals(obj);
    }

    @Override
    public String getString() {
        return Float.toString(value);
    }

    @Override
    public byte getByte() {
        return (byte) value;
    }

    @Override
    public short getShort() {
        return (short) value;
    }

    @Override
    public int getInt() {
        return (int) value;
    }

    @Override
    public long getLong() {
        return (long) value;
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