package me.whileinside.uson.reader;

import me.whileinside.uson.Json;
import me.whileinside.uson.util.CharArraySequence;

import java.io.IOException;

/**
 * @author Unidentified Person
 */
public class BufferedJsonReader implements JsonReader {

    private final char[] buf;
    private int pos;

    public BufferedJsonReader(char[] buf) {
        this.buf = buf;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void finish() {
    }

    @Override
    public int getPosition() {
        return pos;
    }

    @Override
    public int read() {
        return pos == buf.length ? -1 : buf[pos++];
    }

    @Override
    public void rollback() {
        pos--;
    }

    @Override
    public char[] getBuffer() {
        return buf;
    }

    @Override
    public CharSequence getChars(int start, int end) {
        return new CharArraySequence(buf, start, end - start);
    }

    @Override
    public int getInt(int start, int end) {
        return Json.parseInt(new CharArraySequence(buf), start, end);
    }

    @Override
    public long getLong(int start, int end) {
        return Json.parseLong(new CharArraySequence(buf), start, end);
    }

    @Override
    public float getFloat(int start, int end) {
        return Json.parseFloat(new CharArraySequence(buf), start, end);
    }

    @Override
    public double getDouble(int start, int end) {
        return Json.parseDouble(new CharArraySequence(buf), start, end);
    }

    @Override
    public char getChar(int idx) {
        return buf[idx];
    }

    @Override
    public IOException getReadCause() {
        return null;
    }
}
