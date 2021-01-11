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

import java.io.IOException;
import java.io.Reader;

/**
 * @author Unidentified Person
 */
public interface JsonReader {

    int getPosition();

    int read();
    void rollback();

    char[] getBuffer();

    CharSequence getChars(int start, int end);
    int getInt(int start, int end);
    long getLong(int start, int end);
    float getFloat(int start, int end);
    double getDouble(int start, int end);

    IOException getReadCause();

    static JsonReader ofStream(Reader reader, int bufferLength) {
        return new StreamJsonReader(reader, bufferLength);
    }

    static JsonReader ofBuffer(String buffer) {
        return new BufferedJsonReader(buffer);
    }

    static JsonReader ofBuffer(char[] buffer) {
        return new BufferedJsonReader(buffer);
    }
}
