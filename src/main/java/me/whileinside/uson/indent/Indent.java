package me.whileinside.uson.indent;

import me.whileinside.uson.util.CharArraySequence;

import java.util.Arrays;

/**
 * @author Unidentified Person
 */
public final class Indent {

    private final IndentType _type;

    private final int _size;
    private final CharArraySequence _value;

    public Indent(IndentType type, int size) {
        _type = type;
        _size = size;

        char[] value = new char[type.getLength() * size];
        Arrays.fill(value, type.getValue());

        this._value = new CharArraySequence(value);
    }

    public IndentType getType() {
        return _type;
    }

    public int getSize() {
        return _size;
    }

    public CharSequence getValue() {
        return _value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Indent)) return false;

        Indent another = (Indent) o;

        return _type == another._type
                && _size == another._size;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + _type.hashCode();
        result = result * 31 + _size;

        return result;
    }

}
