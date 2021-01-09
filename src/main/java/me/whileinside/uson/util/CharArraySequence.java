package me.whileinside.uson.util;

import org.jetbrains.annotations.NotNull;

/**
 * @author Unidentified Person
 */
public class CharArraySequence implements CharSequence {

    private final char[] chars;
    private final int off, len;
    private int hash;

    public CharArraySequence(char[] chars, int off, int len) {
        this.chars = chars;

        this.off = off;
        this.len = len;
    }

    public CharArraySequence(char[] chars) {
        this(chars, 0, chars.length);
    }

    @Override
    public int length() {
        return len;
    }

    @Override
    public char charAt(int index) {
        if (index >= len) {
            throw new IndexOutOfBoundsException();
        }

        return chars[off + index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new CharArraySequence(chars, off + start, end - start);
    }

    @NotNull
    @Override
    public String toString() {
        return new String(chars, off, len);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof CharArraySequence)) return false;

        CharArraySequence o = (CharArraySequence) obj;

        if (chars.length != o.chars.length) return false;
        if (len != o.len) return false;
        if (off != o.off) return false;

        for (int i = 0; i < len; i++) {
            int idx = i + off;

            if (chars[idx] != o.chars[idx]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public synchronized int hashCode() {
        if (hash != 0) {
            return hash;
        }

        hash = 1;

        for (int i = 0; i < len; i++) {
            hash = hash * 31 + chars[off + i];
        }

        return hash;
    }
}
