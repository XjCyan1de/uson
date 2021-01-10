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

import me.whileinside.uson.Json;
import me.whileinside.uson.util.CharArraySequence;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

/**
 * @author Unidentified Person
 */
public class StreamJsonReader implements JsonReader {

    private final Reader reader;
    private final int bufLen;

    private char[] buf;
    private boolean finished;

    private int pos;
    private int read;

    private IOException readCause;

    public StreamJsonReader(Reader reader, int bufLen) {
        this.reader = reader;
        this.buf = new char[bufLen];
        this.bufLen = bufLen;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void finish() {
        finished = true;
    }

    @Override
    public int getPosition() {
        return pos;
    }

    @Override
    public int read() {
        if (read == pos) {
            try {
                int value = reader.read();

                if (value != -1) {
                    if (pos == buf.length) {
                        buf = Arrays.copyOf(buf, buf.length + bufLen);
                    }

                    buf[pos] = (char) value;
                    pos++;
                    read++;
                }

                return value;
            } catch (IOException e) {
                readCause = e;

                throw new RuntimeException();
            }
        } else {
            return buf[pos++];
        }
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
        return readCause;
    }
}
