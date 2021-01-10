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

package me.whileinside.uson;

import me.whileinside.uson.reader.JsonReader;
import me.whileinside.uson.value.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
public class JsonValue extends JsonNode {

    private Json json;
    private final ValueBackend backend;
    private final boolean string;

    JsonValue(ValueBackend backend, boolean string) {
        this.backend = backend;
        this.string = string;
    }

    JsonValue(Json json, JsonReader reader, int begin, int end, boolean string) {
        this(new BufferBackend(reader, begin, end, json.isCacheBufferedValues()), string);

        this.json = json;
    }

    public JsonValue(boolean value) {
        this(new BooleanBackend(value), false);
    }

    public JsonValue(byte value) {
        this(new ByteBackend(value), false);
    }

    public JsonValue(short value) {
        this(new ShortBackend(value), false);
    }

    public JsonValue(int value) {
        this(new IntegerBackend(value), false);
    }

    public JsonValue(long value) {
        this(new LongBackend(value), false);
    }

    public JsonValue(float value) {
        this(new FloatBackend(value), false);
    }

    public JsonValue(double value) {
        this(new DoubleBackend(value), false);
    }

    public JsonValue(String value) {
        this(new StringBackend(value), true);
    }

    @Override
    public @NotNull String asString() {
        return json == null || json.isAutoUnescape() ? asUnescapedString() : asEscapedString();
    }

    @Override
    public @NotNull String asUnescapedString() {
        return backend.getUnescapedString();
    }

    @Override
    public @NotNull String asEscapedString() {
        return backend.getEscapedString();
    }

    public byte asByte() {
        return backend.getByte();
    }

    public short asShort() {
        return backend.getShort();
    }

    public int asInt() {
        return backend.getInt();
    }

    public long asLong() {
        return backend.getLong();
    }

    public double asDouble() {
        return backend.getDouble();
    }

    public float asFloat() {
        return backend.getFloat();
    }

    @Override
    public @NotNull BigDecimal asBigDecimal() {
        return backend.getBigDecimal();
    }

    @Override
    public boolean asBoolean() {
        return backend.getBoolean();
    }

    @Override
    public int hashCode() {
        return asString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof JsonValue)) return false;

        JsonValue value = (JsonValue) o;

        return asRaw().equals(value.asRaw());
    }

    @Override
    public boolean isString() {
        return string;
    }

    @Override
    public boolean isNumber() {
        return !string;
    }

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public String toString() {
        return "Value[" + (string ? "\"" + asString() + "\"" : asString()) + "]";
    }

    @Override
    public @NotNull CharSequence asRaw() {
        return asEscapedString();
    }
    
}
