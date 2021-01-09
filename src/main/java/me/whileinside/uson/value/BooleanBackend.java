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
    public String getString(boolean escaped) {
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