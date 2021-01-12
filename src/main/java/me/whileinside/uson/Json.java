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

import me.whileinside.uson.indent.Indent;
import me.whileinside.uson.indent.IndentType;
import me.whileinside.uson.reader.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Unidentified Person
 */
public final class Json {

    private static final Indent[][] INDENT_CACHE = new Indent[IndentType.COUNT][];

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private static final Keyword[] KEYWORDS = new Keyword[]
            {
                    new Keyword(JsonBoolean.TRUE),
                    new Keyword(JsonBoolean.FALSE),
                    new Keyword(JsonNull.INSTANCE)
            };

    private static class Keyword {
        private final JsonNode node;
        private final char[] buffer;

        public Keyword(JsonNode node) {
            this.node = node;
            this.buffer = node.asString().toCharArray();
        }

        public boolean startsWith(char value) {
            return buffer[0] == value;
        }

    }

    /* ============================= Json Options =================================== */
    public static final int PRETTY_PRINTING = 1;                              //
    public static final int CACHE_BUFFERED_VALUES = 1 << 1;                         //
    public static final int AUTO_UNESCAPE = 1 << 2;                         //
    public static final int CHECK_RAW_VALUES_ONLY = 1 << 3;                         //
    public static final int DEFAULTS = CACHE_BUFFERED_VALUES | CHECK_RAW_VALUES_ONLY; //
    public static final int NO_OPTIONS = 0;                                           //
    /* ============================================================================== */

    private static final Json defaultInstance = new Json();

    private IndentType _indentType;
    private Indent[] _indentCache;

    private int _bufferLength;
    private int _options;

    public Json() {
        setIndentType(IndentType.ONE_TAB);
        setBufferLength(1024);
        setOptions(DEFAULTS);
    }

    public boolean isAutoUnescape() {
        return hasOptions(AUTO_UNESCAPE);
    }

    public boolean isCacheBufferedValues() {
        return hasOptions(CACHE_BUFFERED_VALUES);
    }

    public boolean isCheckRawValuesOnly() {
        return hasOptions(CHECK_RAW_VALUES_ONLY);
    }

    public boolean isPrettyPrinting() {
        return hasOptions(PRETTY_PRINTING);
    }

    public void setOptions(int options) {
        _options = options;
    }

    public void addOptions(int options) {
        _options |= options;
    }

    public void removeOptions(int options) {
        _options &= ~options;
    }

    public void clearOptions() {
        _options = NO_OPTIONS;
    }

    public boolean hasOptions(int options) {
        return (_options & options) == options;
    }

    public void setIndentType(IndentType indentType) {
        _indentType = indentType;
        _indentCache = INDENT_CACHE[_indentType.ordinal()];
    }

    public void setBufferLength(int bufferLength) {
        _bufferLength = bufferLength;
    }

    public JsonNode fromJson(String json) {
        return fromJson(new JsonReader(json));
    }

    public JsonNode fromJson(char[] json) {
        return fromJson(new JsonReader(json));
    }

    public JsonNode fromJson(InputStream is) throws IOException {
        return fromJson(new InputStreamReader(is));
    }

    public JsonNode fromJson(Reader reader) throws IOException {
        JsonReader jsonReader = new JsonReader(reader, _bufferLength);
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
        Keyword keyword = null;

        int idx = 0;
        int value;

        while ((value = reader.read()) != -1) {
            if (idx == 0) {
                char ch = (char) value;

                for (Keyword el : KEYWORDS) {
                    if (el.startsWith(ch)) {
                        keyword = el;
                        idx++;
                        break;
                    }
                }

                continue;
            }

            if (value == ']' || value == '}' || value == ',' || value == ':' || shouldSkip(value)) {
                reader.rollback();
                break;
            }

            if (idx == keyword.buffer.length || keyword.buffer[idx] != value) {
                throw new IllegalStateException("Unexpected symbol: " + (char) value);
            }

            idx++;
        }

        assert keyword != null;
        return keyword.node;
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

        return new JsonValue(reader, pos, reader.getPosition(), false, this);
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

            while (shouldSkip(value)) {
                value = reader.read();

                if (value == -1) {
                    break;
                }
            }

            if (state == 1 || value == ',') {
                elementKey = fromJson(reader);
                state = 2;
            } else if (value == ':' && state == 2) {
                nodes.put(elementKey.asUnescapedString(), fromJson(reader));
                elementKey = null;

                state = 0;
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
                    return new JsonValue(reader, pos, reader.getPosition() - 1, true, this);
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

            for (; ; ) {
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

            for (; ; ) {
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

            for (; ; ) {
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

            for (; ; ) {
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

        synchronized (INDENT_CACHE) {
            if (_indentCache == null) {
                INDENT_CACHE[_indentType.ordinal()] = _indentCache = new Indent[4];
            }

            if (_indentCache.length <= size) {
                Indent[] indentCache = new Indent[Math.max(_indentCache.length << 1, size)];
                System.arraycopy(_indentCache, 0, indentCache, 0, _indentCache.length);

                INDENT_CACHE[_indentType.ordinal()] = _indentCache = indentCache;
            }

            Indent indent = _indentCache[size];

            if (indent == null || indent.getType() != _indentType) {
                _indentCache[size] = indent = new Indent(_indentType, size);
            }

            return indent;
        }
    }

    public static Json defaultInstance() {
        return defaultInstance;
    }

    static boolean shouldSkip(int value) {
        return value == ' ' || value == '\r' || value == '\n' || value == '\t';
    }

    static char[] toUTF16(int value) {
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

    public static String escape(String unescaped) {
        StringBuilder builder = new StringBuilder(unescaped.length());

        for (int i = 0; i < unescaped.length(); i++) {
            char ch = unescaped.charAt(i);

            switch (ch) {
                case '\"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                case '/':
                    builder.append("\\/");
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
                    if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F' || ch >= '\u2000' && ch <= '\u20FF') {
                        builder.append("\\u").append(toUTF16(ch));
                    } else {
                        builder.append(ch);
                    }

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
                        int result = 0;

                        for (int j = i + 1, end = i + 5; j < end; i++, j++) {
                            char c = escaped.charAt(j);

                            if (c >= '0' && c <= '9') {
                                result = (result << 4) + (c - '0');
                            } else if (c >= 'A' && c <= 'Z') {
                                result = (result << 4) + (c - 'A' + 10);
                            } else if (c >= 'a' && c <= 'z') {
                                result = (result << 4) + (c - 'a' + 10);
                            }
                        }

                        value = (char) result;

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
