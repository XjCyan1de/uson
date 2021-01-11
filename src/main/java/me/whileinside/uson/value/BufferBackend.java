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

import me.whileinside.uson.Json;
import me.whileinside.uson.reader.JsonReader;
import me.whileinside.uson.util.Lazy;

import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
public final class BufferBackend extends ValueBackend {

    private final JsonReader reader;
    private final int begin, end;

    private final Json json;

    private Lazy<String> unescaped, escaped;

    public BufferBackend(JsonReader reader, int begin, int end, Json json) {
        this.reader = reader;
        this.begin = begin;
        this.end = end;
        this.json = json;

        if (json.isCacheBufferedValues()) {
            this.unescaped = Lazy.of(this::_getUnescapedString);
            this.escaped = Lazy.of(this::_getEscapedString);
        }
    }

    private String _getUnescapedString() {
        return Json.unescape(getEscapedString());
    }

    private String _getEscapedString() {
        return getRaw().toString();
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + begin;
        hash = hash * 31 + end;
        hash = hash * 31 + reader.hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ValueBackend)) return false;

        if (obj instanceof BufferBackend) {
            BufferBackend that = (BufferBackend) obj;

            if (reader == that.reader && begin == that.begin && end == that.end) {
                return true;
            }
        }

        ValueBackend that = (ValueBackend) obj;

        return json.isCheckRawValuesOnly()
                ? getRaw().equals(that.getRaw())
                : getUnescapedString().equals(that.getUnescapedString());
    }

    @Override
    public CharSequence getRaw() {
        return reader.getChars(begin, end);
    }

    @Override
    public String getString() {
        return json.isAutoUnescape() ? getUnescapedString() : getEscapedString();
    }

    @Override
    public String getUnescapedString() {
        return unescaped == null ? _getUnescapedString() : unescaped.get();
    }

    @Override
    public String getEscapedString() {
        return escaped == null ? _getEscapedString() : escaped.get();
    }

    @Override
    public byte getByte() {
        return (byte) getInt();
    }

    @Override
    public short getShort() {
        return (short) getInt();
    }

    @Override
    public int getInt() {
        return reader.getInt(begin, end);
    }

    @Override
    public long getLong() {
        return reader.getLong(begin, end);
    }

    @Override
    public double getDouble() {
        return reader.getDouble(begin, end);
    }

    @Override
    public float getFloat() {
        return reader.getFloat(begin, end);
    }

    @Override
    public boolean getBoolean() {
        return false;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return new BigDecimal(reader.getBuffer(), begin, end - begin);
    }
}
