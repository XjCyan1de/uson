package me.whileinside.uson;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
public abstract class JsonNode {

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isValue() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public @NotNull CharSequence asRaw() {
        throw new UnsupportedOperationException();
    }

    /** cast node as array */
    public @NotNull JsonArray asArray() {
        throw new ClassCastException();
    }

    /** cast node as value */
    public @NotNull JsonValue asValue() {
        throw new ClassCastException();
    }

    /** cast node as object */
    public @NotNull JsonObject asObject() {
        throw new ClassCastException();
    }

    /** cast node as string */
    public @NotNull String asString() {
        throw new UnsupportedOperationException();
    }

    /** cast node as escaped string */
    public @NotNull String asEscapedString() {
        throw new UnsupportedOperationException();
    }
    public @NotNull String asUnescapedString() {
        throw new UnsupportedOperationException();
    }

    /** cast node as byte */
    public byte asByte() {
        throw new UnsupportedOperationException();
    }

    /** cast node as short */
    public short asShort() {
        throw new UnsupportedOperationException();
    }

    /** cast node as int */
    public int asInt() {
        throw new UnsupportedOperationException();
    }

    /** cast node as long */
    public long asLong() {
        throw new UnsupportedOperationException();
    }

    /** cast node as float */
    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    /** cast node as double */
    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    /** cast node as boolean */
    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    /** cast node as big decimal */
    public @NotNull BigDecimal asBigDecimal() {
        throw new UnsupportedOperationException();
    }

}
