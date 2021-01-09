package me.whileinside.uson;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Unidentified Person
 */
public class JsonArray extends JsonNode  {

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

    public JsonArray add(@NotNull String value) {
        return null;
    }

    public JsonArray add(byte value) {
        return null;
    }

    public JsonArray add(short value) {
        return null;
    }

    public JsonArray add(int value) {
        return null;
    }

    public JsonArray add(long value) {
        return null;
    }

    public JsonArray add(float value) {
        return null;
    }

    public JsonArray add(double value) {
        return null;
    }

    public JsonArray add(boolean value) {
        return null;
    }

    public JsonArray add(@NotNull BigDecimal value) {
        return null;
    }

}
