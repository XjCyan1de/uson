package me.whileinside.uson.value;

import me.whileinside.uson.Json;

import java.math.BigDecimal;

public final class StringBackend implements ValueBackend {

    private final String value;
    private String escaped;

    public StringBackend(String value) {
        this.value = value;
        this.escaped = Json.escape(value);
    }

    @Override
    public String getEscapedString() {
        if (escaped == null) {
            escaped = Json.escape(value);
        }

        return escaped;
    }

    @Override
    public CharSequence getRaw() {
        return getEscapedString();
    }

    @Override
    public String getString(boolean escaped) {
        return escaped ? getEscapedString() : value;
    }

    @Override
    public String getUnescapedString() {
        return value;
    }

    @Override
    public byte getByte() {
        return Byte.parseByte(value);
    }

    @Override
    public short getShort() {
        return Short.parseShort(value);
    }

    @Override
    public int getInt() {
        return Integer.parseInt(value);
    }

    @Override
    public long getLong() {
        return Long.parseLong(value);
    }

    @Override
    public double getDouble() {
        return Double.parseDouble(value);
    }

    @Override
    public float getFloat() {
        return Float.parseFloat(value);
    }

    @Override
    public boolean getBoolean() {
        return value.equals("true");
    }

    @Override
    public BigDecimal getBigDecimal() {
        return null;
    }
}