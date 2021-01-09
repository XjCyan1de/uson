package me.whileinside.uson;

import me.whileinside.uson.reader.JsonReader;
import me.whileinside.uson.value.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
        this(new BufferBackend(reader, begin, end, json.isCacheBufferValues()), string);

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
