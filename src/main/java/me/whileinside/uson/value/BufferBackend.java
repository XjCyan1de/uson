package me.whileinside.uson.value;

import me.whileinside.uson.Json;
import me.whileinside.uson.cache.Lazy;
import me.whileinside.uson.reader.JsonReader;

import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
public class BufferBackend implements ValueBackend {

    private final JsonReader reader;
    private final int begin, end;

    private Lazy<String> unescaped, escaped;

    public BufferBackend(JsonReader reader, int begin, int end, boolean cache) {
        this.reader = reader;
        this.begin = begin;
        this.end = end;

        if (cache) {
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
    public CharSequence getRaw() {
        if (!reader.isFinished()) {
            throw new RuntimeException();
        }

        return reader.getChars(begin, end);
    }

    @Override
    public String getString(boolean escaped) {
        return escaped ? getEscapedString() : getUnescapedString();
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
        return Json.parseInt(getRaw());
    }

    @Override
    public long getLong() {
        return Json.parseLong(getRaw());
    }

    @Override
    public double getDouble() {
        return Json.parseDouble(getRaw());
    }

    @Override
    public float getFloat() {
        return Json.parseFloat(getRaw());
    }

    @Override
    public boolean getBoolean() {
        if (!reader.isFinished()) {
            throw new RuntimeException();
        }

        int len = end - begin;

        if (len == 1) {
            return reader.getChar(begin) == '1';
        } else if (len == 4) {
            return reader.getChar(begin) == 't'
                    && reader.getChar(begin + 1) == 'r'
                    && reader.getChar(begin + 2) == 'u'
                    && reader.getChar(begin + 3) == 'e';
        } else {
            return false;
        }
    }

    @Override
    public BigDecimal getBigDecimal() {
        if (!reader.isFinished()) {
            throw new RuntimeException();
        }

        return new BigDecimal(reader.getBuffer(), begin, end - begin);
    }
}
