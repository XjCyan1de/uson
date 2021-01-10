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

package me.whileinside.uson;

import me.whileinside.uson.reader.BufferedJsonReader;
import me.whileinside.uson.reader.JsonReader;
import me.whileinside.uson.reader.StreamJsonReader;
import me.whileinside.uson.indent.Indent;
import me.whileinside.uson.indent.IndentType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Unidentified Person
 */
public final class Json {

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private static final char[] TRUE = "true".toCharArray(),
            FALSE = "false".toCharArray(),
            NULL = "null".toCharArray();

    public static final int PRETTY_PRINTING         = 1;
    public static final int CACHE_BUFFERED_VALUES   = 1 << 1;
    public static final int AUTO_UNESCAPE           = 1 << 2;

    private static final Json defaultInstance = new Json();

    private IndentType _indentType = IndentType.ONE_TAB;
    private Indent[] _indentCache = new Indent[4];

    private int _bufSize;
    private int _options;

    public static Json defaultInstance() {
        return defaultInstance;
    }

    private static boolean shouldSkip(int value) {
        return value == ' ' || value == '\r' || value == '\n' || value == '\t';
    }

    public boolean isAutoUnescape() {
        return (_options & AUTO_UNESCAPE) != 0;
    }

    public boolean isCacheBufferValues() {
        return (_options & CACHE_BUFFERED_VALUES) != 0;
    }

    public boolean isPrettyPrinting() {
        return (_options & PRETTY_PRINTING) != 0;
    }

    public void setOptions(int options) {
        _options =  options;
    }

    public void setIndentType(IndentType type) {
        _indentType = type;
    }

    public void setBufferLength(int len) {
        _bufSize = len;
    }

    public JsonNode fromJson(String json) {
        return fromJson(new BufferedJsonReader(json.toCharArray()));
    }

    public JsonNode fromJson(char[] json) {
        return fromJson(new BufferedJsonReader(json));
    }

    public JsonNode fromJson(InputStream is) throws IOException {
        return fromJson(new InputStreamReader(is));
    }

    public JsonNode fromJson(Reader reader) throws IOException {
        JsonReader jsonReader = new StreamJsonReader(reader, _bufSize);
        JsonNode node;

        try {
            node = fromJson(jsonReader);
        } catch (RuntimeException e) {
            IOException readCause = jsonReader.getReadCause();

            if (readCause != null) {
                throw readCause;
            }

            throw e;
        }

        jsonReader.finish();

        return node;
    }

    public JsonNode fromJson(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            return fromJson(reader);
        }
    }

    public JsonNode fromJson(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return fromJson(reader);
        }
    }

    JsonNode fromJson(JsonReader reader) {
        int value;

        do {
            value = reader.read();
        } while (value != -1 && shouldSkip(value));

        if (value == -1) {
            throw new IllegalStateException("Unexpected end of stream!");
        }

        if (value == '-' || (value >= '0' && value <= '9')) {
            reader.rollback();

            return parseNumber(reader);
        }

        if (value == 'n' || value == 't' || value == 'f') {
            reader.rollback();

            return parseKeyword(reader);
        }

        switch (value) {
            case '\"':
                return parseString(reader);
            case '{':
                return parseObject(reader);
            case '[':
                return parseArray(reader);
        }

        throw new IllegalStateException("Unexpected symbol: " + (char) value);
    }

    JsonNode parseKeyword(JsonReader reader) {
        int type = 0;
        char[] buf = null;

        int idx = 0;
        int value;

        while ((value = reader.read()) != -1) {
            if (idx == 0) {
                switch (value) {
                    case 't':
                        type = 0;
                        buf = TRUE;
                        break;
                    case 'f':
                        type = 1;
                        buf = FALSE;
                        break;
                    case 'n':
                        type = 2;
                        buf = NULL;
                        break;
                }
            }

            if (value == ']' || value == '}' || value == ',' || value == ':' || shouldSkip(value)) {
                reader.rollback();
                break;
            }

            if (buf == null || idx == buf.length || buf[idx] != value) {
                throw new IllegalStateException("Unexpected symbol: " + (char) value);
            }

            idx++;
        }

        switch (type) {
            case 0:
                return JsonBoolean.TRUE;
            case 1:
                return JsonBoolean.FALSE;
            case 2:
                return JsonNull.INSTANCE;
            default:
                throw new RuntimeException();
        }
    }

    JsonValue parseNumber(JsonReader reader) {
        int pos = reader.getPosition();
        int value;

        while ((value = reader.read()) != -1) {
            if (value == ']' || value == '}' || value == ',' || value == ':' || shouldSkip(value)) {
                reader.rollback();
                break;
            }
        }

        return new JsonValue(this, reader, pos, reader.getPosition(), false);
    }

    JsonObject parseObject(JsonReader reader) {
        int state = 0;
        int value;
        Map<String, JsonNode> nodes = null;

        JsonNode elementKey = null;

        while ((value = reader.read()) != -1) {
            if (nodes == null) {
                if (shouldSkip(value)) {
                    continue;
                }

                if (value == '}') {
                    return JsonObject.EMPTY;
                }

                nodes = new LinkedHashMap<>();
                state = 1;

                reader.rollback();
            }

            if (state == 1) {
                elementKey = fromJson(reader);
                state = 2;
            } else {
                nodes.put(elementKey.asUnescapedString(), fromJson(reader));
                elementKey = null;

                state = 0;
            }

            // skip all spaces and tabs
            while (shouldSkip(value = reader.read()));

            if (value == ',') {
                state = 1;
            } else if (value == ':' && state == 2) {
                state = 3;
            } else if (value == '}') {
                return new JsonObject(nodes);
            } else {
                throw new IllegalStateException("Unexpected symbol: " + (char) value);
            }
        }

        throw new IllegalStateException("End of data");
    }

    JsonArray parseArray(JsonReader reader) {
        int value;

        boolean nodeBegin = false;
        List<JsonNode> nodes = null;

        while ((value = reader.read()) != -1) {
            if (shouldSkip(value)) {
                continue;
            }

            if (nodes == null) {
                if (value == ']') {
                    return JsonArray.EMPTY;
                }

                nodes = new ArrayList<>();
                nodeBegin = true;
            }

            if (nodeBegin) {
                reader.rollback();

                nodes.add(fromJson(reader));
                nodeBegin = false;
            }

            if (value == ',') {
                nodeBegin = true;
            } else if (value == ']') {
                return new JsonArray(nodes);
            }
        }

        throw new IllegalStateException("End of data");
    }

    JsonValue parseString(JsonReader reader) {
        int pos = reader.getPosition();
        int value;

        boolean escape = false;

        while ((value = reader.read()) != -1) {
            if (escape) {
                escape = false;
            } else {
                if (value == '\\') {
                    escape = true;
                } else if (value == '\"') {
                    return new JsonValue(this, reader, pos, reader.getPosition() - 1, true);
                }
            }
        }

        throw new IllegalStateException("End of data");
    }

    private void toPrettyJson(JsonNode node, Appendable appendable, int tabs) throws IOException {
        _toJson(node, appendable);

        if (node.isArray()) {
            appendable.append('[');

            Iterator<JsonNode> elements = node.asArray().nodeIterator();

            if (!elements.hasNext()) {
                appendable.append(' ');
                appendable.append(']');
                return;
            }

            appendable.append('\n');

            for (;;) {
                appendable.append(getIndentString(tabs));
                toPrettyJson(elements.next(), appendable, tabs + 1);

                if (!elements.hasNext()) {
                    break;
                }

                appendable.append(',');
                appendable.append('\n');
            }

            appendable.append('\n');
            appendable.append(getIndentString(tabs - 1));
            appendable.append(']');
        } else if (node.isObject()) {
            appendable.append('{');

            Iterator<Map.Entry<String, JsonNode>> elements = node.asObject().nodeIterator();

            if (!elements.hasNext()) {
                appendable.append(' ');
                appendable.append('}');
                return;
            }

            appendable.append('\n');

            for (;;) {
                Map.Entry<String, JsonNode> element = elements.next();

                appendable.append(getIndentString(tabs));
                appendable.append('"');
                appendable.append(element.getKey());
                appendable.append('"');
                appendable.append(':');
                appendable.append(' ');

                toPrettyJson(element.getValue(), appendable, tabs + 1);

                if (!elements.hasNext()) {
                    break;
                }

                appendable.append(',');
                appendable.append('\n');
            }

            appendable.append('\n');
            appendable.append(getIndentString(tabs - 1));
            appendable.append('}');
        }
    }

    private void toSimpleJson(JsonNode node, Appendable appendable) throws IOException {
        _toJson(node, appendable);

        if (node.isArray()) {
            appendable.append('[');

            Iterator<JsonNode> elements = node.asArray().nodeIterator();

            if (!elements.hasNext()) {
                appendable.append(']');
                return;
            }

            for (;;) {
                toJson(elements.next(), appendable);

                if (!elements.hasNext()) {
                    break;
                }

                appendable.append(',');
            }

            appendable.append(']');
        } else if (node.isObject()) {
            appendable.append('{');

            Iterator<Map.Entry<String, JsonNode>> elements = node.asObject()
                    .nodeIterator();

            if (!elements.hasNext()) {
                appendable.append('}');
                return;
            }

            for (;;) {
                Map.Entry<String, JsonNode> element = elements.next();

                appendable.append('"');
                appendable.append(element.getKey());
                appendable.append('"');
                appendable.append(':');
                toJson(element.getValue(), appendable);

                if (!elements.hasNext()) {
                    break;
                }

                appendable.append(',');
            }

            appendable.append('}');
        }
    }

    private void _toJson(JsonNode node, Appendable appendable) throws IOException {
        if (node.isNull()) {
            appendable.append("null");
        } else if (node.isBoolean()) {
            appendable.append(node.asRaw());
        } else if (node.isValue()) {
            if (node.isString()) {
                appendable.append('"');
            }

            appendable.append(node.asRaw());

            if (node.isString()) {
                appendable.append('"');
            }
        }
    }

    public void toJson(JsonNode node, Appendable appendable) throws IOException {
        if (isPrettyPrinting()) {
            toPrettyJson(node, appendable, 1);
        } else {
            toSimpleJson(node, appendable);
        }
    }

    public void toJson(JsonNode node, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            toJson(node, writer);
        }
    }

    public void toJson(JsonNode node, Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            toJson(node, writer);
        }
    }

    public String toJson(JsonNode node) {
        StringBuilder sb = new StringBuilder();

        try {
            toJson(node, sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    CharSequence getIndentString(int size) {
        return getIndent(size).getValue();
    }

    Indent getIndent(int size) {
        if (size < 0) throw new IllegalArgumentException("size must be greater than or equal to zero");

        if (_indentCache.length <= size) {
            Indent[] indentCache = new Indent[Math.max(_indentCache.length*2, size)];
            System.arraycopy(_indentCache, 0, indentCache, 0, _indentCache.length);

            _indentCache = indentCache;
        }

        Indent indent = _indentCache[size];

        if (indent == null || indent.getType() != _indentType) {
            _indentCache[size] = indent = new Indent(_indentType, size);
        }

        return indent;
    }

    public static int parseUTF16(CharSequence sequence) {
        return parseUTF16(sequence, 0, sequence.length());
    }

    public static int parseUTF16(CharSequence sequence, int start, int end) {
        int result = 0;

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c >= '0' && c <= '9') {
                result = (result << 4) + (c - '0');
            } else if (c >= 'A' && c <= 'Z') {
                result = (result << 4) + (c - 'A' + 10);
            } else if (c >= 'a' && c <= 'z') {
                result = (result << 4) + (c - 'a' + 10);
            }
        }

        return result;
    }

    public static char[] toUTF16(int value) {
        char[] result = new char[4];

        for (int i = 3; i >= 0; i--) {
            if (value > 0) {
                result[i] = HEX[value & 0xF];
                value >>= 4;
            } else {
                result[i] = '0';
            }
        }

        return result;
    }

    public static long parseLong(CharSequence sequence) {
        return parseLong(sequence, 0, sequence.length());
    }

    public static long parseLong(CharSequence sequence, int start, int end) {
        boolean negate = false;
        long result = 0;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c < '0' || c > '9') {
                continue;
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static int parseInt(CharSequence sequence) {
        return parseInt(sequence, 0, sequence.length());
    }

    public static int parseInt(CharSequence sequence, int start, int end) {
        boolean negate = false;
        int result = 0;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c < '0' || c > '9') {
                continue;
            }

            result = result * 10 + (c & 0xF);
        }

        return negate ? -result : result;
    }

    public static double parseDouble(CharSequence sequence) {
        return parseDouble(sequence, 0, sequence.length());
    }

    public static double parseDouble(CharSequence sequence, int start, int end) {
        boolean negate = false;

        int x = 0;
        int y = 0;
        int yDivision = 1;

        boolean dot = false;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == '.') {
                dot = true;
                continue;
            }

            if (c < '0' || c > '9') {
                continue;
            }

            int v = c & 0xF;

            if (dot) {
                y = y * 10 + v;
                yDivision *= 10;
            } else {
                x = x * 10 + v;
            }
        }

        double result = x + y / (double) yDivision;

        return negate ? -result : result;
    }

    public static float parseFloat(CharSequence sequence) {
        return parseFloat(sequence, 0, sequence.length());
    }

    public static float parseFloat(CharSequence sequence, int start, int end) {
        boolean negate = false;

        int x = 0;
        int y = 0;
        int yDivision = 1;

        boolean dot = false;

        switch (sequence.charAt(start)) {
            case '-':
                negate = true;
            case '+':
                start++;
                break;
        }

        for (int i = start; i < end; i++) {
            char c = sequence.charAt(i);

            if (c == '.') {
                dot = true;
                continue;
            }

            if (c < '0' || c > '9') {
                continue;
            }

            int v = c & 0xF;

            if (dot) {
                y = y * 10 + v;
                yDivision *= 10;
            } else {
                x = x * 10 + v;
            }
        }

        float result = x + y / (float) yDivision;

        return negate ? -result : result;
    }

    public static String escape(String unescaped) {
        StringBuilder builder = new StringBuilder(unescaped.length());

        for (char value : unescaped.toCharArray()) {
            if (value > 256) {
                builder.append("\\u").append(toUTF16(value));
                continue;
            }

            switch (value) {
                case '\\':
                    builder.append("\\\\");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                default:
                    builder.append(value);
                    break;
            }
        }

        return builder.toString();
    }

    public static String unescape(String escaped) {
        StringBuilder builder = new StringBuilder(escaped.length());
        boolean backslash = false;

        for (int i = 0; i < escaped.length(); i++) {
            char value = escaped.charAt(i);

            if (!backslash && value == '\\') {
                backslash = true;
                continue;
            }

            if (backslash) {
                switch (value) {
                    case 'u':
                        value = (char) parseUTF16(escaped, i + 1, i + 5);
                        i += 4;
                        break;
                    case 'r':
                        value = '\r';
                        break;
                    case 'n':
                        value = '\n';
                        break;
                    case 't':
                        value = '\t';
                        break;
                    case 'b':
                        value = '\b';
                        break;
                    case 'f':
                        value = '\f';
                        break;
                }

                backslash = false;
            }

            builder.append(value);
        }

        return builder.toString();
    }

}
