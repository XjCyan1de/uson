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

import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
public final class BufferBackend extends ValueBackend {

    private final JsonReader reader;
    private final int begin, end;

    private final Json json;
    private String unescaped, escaped;

    public BufferBackend(JsonReader reader, int begin, int end, Json json) {
        this.reader = reader;
        this.begin = begin;
        this.end = end;
        this.json = json;
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

        return super.equals(obj);
    }

    @Override
    protected boolean checkRawValues() {
        return json.isCheckRawValues();
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
        if (json.isCacheBufferedValues()) {
            if (unescaped == null) {
                unescaped = _getUnescapedString();
            }

            return unescaped;
        }

        return _getUnescapedString();
    }

    @Override
    public String getEscapedString() {
        if (json.isCacheBufferedValues()) {
            if (escaped == null) {
                escaped = _getEscapedString();
            }

            return escaped;
        }

        return _getEscapedString();
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
