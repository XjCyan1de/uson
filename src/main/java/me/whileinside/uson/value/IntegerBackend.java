package me.whileinside.uson.value;

import java.math.BigDecimal;

public final class IntegerBackend implements ValueBackend {

    private final int value;

    public IntegerBackend(int value) {
        this.value = value;
    }

    @Override
    public String getEscapedString() {
        return Integer.toString(value);
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
        return value;
    }

    @Override
    public long getLong() {
        return value;
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
        return value == 1;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return new BigDecimal(value);
    }

}