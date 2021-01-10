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

public final class BooleanBackend implements ValueBackend {

    private final boolean value;

    public BooleanBackend(boolean value) {
        this.value = value;
    }

    @Override
    public String getEscapedString() {
        return value ? "true" : "false";
    }

    @Override
    public CharSequence getRaw() {
        return getEscapedString();
    }

    @Override
    public String getString() {
        return getEscapedString();
    }

    @Override
    public String getUnescapedString() {
        return getEscapedString();
    }

    @Override
    public byte getByte() {
        throw new NumberFormatException();
    }

    @Override
    public short getShort() {
        throw new NumberFormatException();
    }

    @Override
    public int getInt() {
        throw new NumberFormatException();
    }

    @Override
    public long getLong() {
        throw new NumberFormatException();
    }

    @Override
    public double getDouble() {
        throw new NumberFormatException();
    }

    @Override
    public float getFloat() {
        throw new NumberFormatException();
    }

    @Override
    public boolean getBoolean() {
        return value;
    }

    @Override
    public BigDecimal getBigDecimal() {
        throw new NumberFormatException();
    }
}