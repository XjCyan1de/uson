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
