package me.whileinside.uson.value;

import java.math.BigDecimal;

public final class FloatBackend implements ValueBackend {

    private final float value;

    public FloatBackend(float value) {
        this.value = value;
    }

    @Override
    public String getEscapedString() {
        return Float.toString(value);
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