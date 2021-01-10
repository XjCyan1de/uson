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

public final class DoubleBackend implements ValueBackend {

    private final double value;

    public DoubleBackend(double value) {
        this.value = value;
    }

    @Override
    public String getEscapedString() {
        return Double.toString(value);
    }

    @Override
    public CharSequence getRaw() {
        return getEscapedString();
    }

    @Override
    public String getString(boolean escaped) {
        return getEscapedString();
    }

    @Override
    public String getUnescapedString() {
        return getEscapedString();
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
        return (float) value;
    }

    @Override
    public boolean getBoolean() {
        return value == 1;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return new BigDecimal(value);
    }

}