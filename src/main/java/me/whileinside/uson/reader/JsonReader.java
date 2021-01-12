/*
 *    Copyright 2021 Unidentified Person
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.whileinside.uson.reader;

import me.whileinside.uson.util.CharArraySequence;
import me.whileinside.uson.util.NumberParser;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

/**
 * @author Unidentified Person
 */
public final class JsonReader {

    private final Reader _reader;
    private final int _bufferLength;

    private char[] _buffer;

    private int _pos;
    private int _read;

    private IOException _readCause;

    public JsonReader(Reader reader, int bufferLength) {
        _reader = reader;
        _buffer = new char[_bufferLength = bufferLength];
    }

    public JsonReader(char[] buffer) {
        _reader = null;
        _bufferLength = 0;
        _buffer = buffer;
        _read = buffer.length;
    }

    public JsonReader(String buffer) {
        _reader = null;
        _bufferLength = 0;
        _buffer = buffer.toCharArray();
        _read = buffer.length();
    }

    public int getPosition() {
        return _pos;
    }

    public int read() {
        if (_read == _pos) {
            if (_reader == null) {
                return -1;
            }

            try {
                int value = _reader.read();

                if (value != -1) {
                    if (_pos == _buffer.length) {
                        _buffer = Arrays.copyOf(_buffer, _buffer.length + _bufferLength);
                    }

                    _buffer[_pos] = (char) value;
                    _pos++;
                    _read++;
                }

                return value;
            } catch (IOException e) {
                _readCause = e;

                throw new RuntimeException();
            }
        } else {
            return _buffer[_pos++];
        }
    }

    public void rollback() {
        _pos--;
    }

    public char[] getBuffer() {
        return _buffer;
    }

    public CharSequence getChars(int start, int end) {
        return new CharArraySequence(_buffer, start, end - start);
    }

    public int getInt(int start, int end) {
        return NumberParser.parseInt(_buffer, start, end);
    }

    public long getLong(int start, int end) {
        return NumberParser.parseLong(_buffer, start, end);
    }

    public float getFloat(int start, int end) {
        return NumberParser.parseFloat(_buffer, start, end);
    }

    public double getDouble(int start, int end) {
        return NumberParser.parseDouble(_buffer, start, end);
    }

    public IOException getReadCause() {
        return _readCause;
    }

}
