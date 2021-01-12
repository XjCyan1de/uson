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

import me.whileinside.uson.indent.IndentType;
import me.whileinside.uson.indent.Indents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Unidentified Person
 */
public class JsonArray extends JsonNode {

    public static final JsonArray EMPTY = new JsonArray(Collections.emptyList());

    private final List<JsonNode> _nodes;

    public JsonArray(int capacity) {
        this(new ArrayList<>(capacity));
    }

    public JsonArray() {
        this(10);
    }

    public JsonArray(JsonNode... nodes) {
        this(Arrays.asList(nodes));
    }

    public JsonArray(String... values) {
        _nodes = new ArrayList<>(values.length);

        for (String value : values) {
            add(value);
        }
    }

    public JsonArray(int... values) {
        _nodes = new ArrayList<>(values.length);

        for (int value : values) {
            add(value);
        }
    }

    public JsonArray(double... values) {
        _nodes = new ArrayList<>(values.length);

        for (double value : values) {
            add(value);
        }
    }

    public JsonArray(float... values) {
        _nodes = new ArrayList<>(values.length);

        for (float value : values) {
            add(value);
        }
    }

    public JsonArray(long... values) {
        _nodes = new ArrayList<>(values.length);

        for (long value : values) {
            add(value);
        }
    }

    public JsonArray(byte... values) {
        _nodes = new ArrayList<>(values.length);

        for (byte value : values) {
            add(value);
        }
    }

    public JsonArray(short... values) {
        _nodes = new ArrayList<>(values.length);

        for (short value : values) {
            add(value);
        }
    }

    public JsonArray(boolean... values) {
        _nodes = new ArrayList<>(values.length);

        for (boolean value : values) {
            add(value);
        }
    }

    public JsonArray(BigDecimal... values) {
        _nodes = new ArrayList<>(values.length);

        for (BigDecimal value : values) {
            add(value);
        }
    }

    public JsonArray(Collection<JsonNode> nodes) {
        this(new ArrayList<>(nodes));
    }

    @ApiStatus.Internal
    JsonArray(List<JsonNode> nodes) {
        _nodes = nodes;
    }

    public @NotNull Iterator<JsonNode> nodeIterator() {
        return nodes().iterator();
    }

    public @NotNull Collection<JsonNode> nodes() {
        return _nodes;
    }

    public boolean hasNodes() {
        return !nodes().isEmpty();
    }

    public int count() {
        return nodes().size();
    }

    public JsonArray add(@NotNull JsonNode node) {
        nodes().add(node);

        return this;
    }

    @Override
    public String toString() {
        return "Array[" + _nodes.stream().map(JsonNode::toString).collect(Collectors.joining(", ")) + "]";
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public @NotNull JsonArray asArray() {
        return this;
    }

    @Override
    public void toSimpleJson(Appendable appendable) throws IOException {
        appendable.append('[');

        Iterator<JsonNode> elements = _nodes.iterator();

        if (!elements.hasNext()) {
            appendable.append(']');
            return;
        }

        for (; ; ) {
            elements.next().toSimpleJson(appendable);

            if (!elements.hasNext()) {
                break;
            }

            appendable.append(',');
        }

        appendable.append(']');
    }

    @Override
    void toPrettyJson(Appendable appendable, IndentType indentType, int tabs) throws IOException {
        appendable.append('[');

        Iterator<JsonNode> elements = _nodes.iterator();

        if (!elements.hasNext()) {
            appendable.append(' ');
            appendable.append(']');
            return;
        }

        appendable.append('\n');

        for (; ; ) {
            appendable.append(Indents.getIndentString(indentType, tabs));
            elements.next().toPrettyJson(appendable, indentType, tabs + 1);

            if (!elements.hasNext()) {
                break;
            }

            appendable.append(',');
            appendable.append('\n');
        }

        appendable.append('\n');
        appendable.append(Indents.getIndentString(indentType, tabs - 1));
        appendable.append(']');
    }

    public JsonArray add(@NotNull String value) {
        return add(new JsonValue(value));
    }

    public JsonArray add(byte value) {
        return add(new JsonValue(value));
    }

    public JsonArray add(short value) {
        return add(new JsonValue(value));
    }

    public JsonArray add(int value) {
        return add(new JsonValue(value));
    }

    public JsonArray add(long value) {
        return add(new JsonValue(value));
    }

    public JsonArray add(float value) {
        return add(new JsonValue(value));
    }

    public JsonArray add(double value) {
        return add(new JsonValue(value));
    }

    public JsonArray add(boolean value) {
        return add(JsonBoolean.valueOf(value));
    }

    public JsonArray add(@NotNull BigDecimal value) {
        return add(new JsonValue(value));
    }

    public JsonArray addNull() {
        return add(JsonNull.INSTANCE);
    }

    @Override
    public int hashCode() {
        return _nodes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof JsonArray)) return false;

        return _nodes.equals(((JsonArray) obj)._nodes);
    }

}
