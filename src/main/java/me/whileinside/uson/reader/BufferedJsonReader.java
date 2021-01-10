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

/**
 * @author Unidentified Person
 */
final class BufferedJsonReader implements JsonReader {

    private final char[] _buffer;
    private int _pos;

    public BufferedJsonReader(char[] buffer) {
        _buffer = buffer;
    }

    @Override
    public int getPosition() {
        return _pos;
    }

    @Override
    public int read() {
        return _pos == _buffer.length ? -1 : _buffer[_pos++];
    }

    @Override
    public void rollback() {
        _pos--;
    }

    @Override
    public char[] getBuffer() {
        return _buffer;
    }

    @Override
    public CharSequence getChars(int start, int end) {
        return new CharArraySequence(_buffer, start, end - start);
    }

    @Override
    public int getInt(int start, int end) {
        return NumberParser.parseInt(new CharArraySequence(_buffer), start, end);
    }

    @Override
    public long getLong(int start, int end) {
        return NumberParser.parseLong(new CharArraySequence(_buffer), start, end);
    }

    @Override
    public float getFloat(int start, int end) {
        return NumberParser.parseFloat(new CharArraySequence(_buffer), start, end);
    }

    @Override
    public double getDouble(int start, int end) {
        return NumberParser.parseDouble(new CharArraySequence(_buffer), start, end);
    }

    @Override
    public IOException getReadCause() {
        return null;
    }
}
