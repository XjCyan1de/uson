package me.whileinside.uson;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Unidentified Person
 */
public class JsonObject extends JsonNode {

    public static final JsonObject EMPTY = new JsonObject(Collections.emptyMap());

    private final Map<String, JsonNode> _nodes;

    public JsonObject(int capacity) {
        this(new LinkedHashMap<>(capacity));
    }

    public JsonObject() {
        this(10);
    }

    @ApiStatus.Internal
    JsonObject(Map<String, JsonNode> nodes) {
        _nodes = nodes;
    }

    public static JsonObject valueOf(Map<String, JsonNode> nodes) {
        return new JsonObject(new LinkedHashMap<>(nodes));
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public @NotNull JsonObject asObject() {
        return this;
    }

    @Override
    public String toString() {
        return "Object[" + _nodes.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", ")) + "]";
    }

    public @NotNull Iterator<Map.Entry<String, JsonNode>> nodeIterator() {
        return nodes().iterator();
    }

    public @NotNull Collection<Map.Entry<String, JsonNode>> nodes() {
        return _nodes.entrySet();
    }

    public boolean hasNodes() {
        return !_nodes.isEmpty();
    }

    public int count() {
        return _nodes.size();
    }

    public boolean hasNode(String name) {
        return _nodes.containsKey(name);
    }

    public JsonObject put(String name, JsonNode value) {
        _nodes.put(name, value);

        return this;
    }

    public JsonObject put(String name, String value) {
        return put(name, new JsonValue(value));
    }

    public JsonObject put(String name, byte value) {
        return put(name, new JsonValue(value));
    }

    public JsonObject put(String name, short value) {
        return put(name, new JsonValue(value));
    }

    public JsonObject put(String name, int value) {
        return put(name, new JsonValue(value));
    }

    public JsonObject put(String name, long value) {
        return put(name, new JsonValue(value));
    }

    public JsonObject put(String name, float value) {
        return put(name, new JsonValue(value));
    }

    public JsonObject put(String name, double value) {
        return put(name, new JsonValue(value));
    }

    public JsonObject put(String name, boolean value) {
        return put(name, new JsonValue(value));
    }

    public void remove(String name) {
        _nodes.remove(name);
    }

    public byte getByte(String name, byte def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asByte();
    }

    public byte getByte(String name) {
        return getByte(name, (byte) 0);
    }

    public short getShort(String name, short def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asShort();
    }

    public short getShort(String name) {
        return getShort(name, (short) 0);
    }

    public int getInt(String name, int def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asInt();
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public long getLong(String name, long def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asLong();
    }

    public long getLong(String name) {
        return getLong(name, 0);
    }

    public float getFloat(String name, float def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asFloat();
    }

    public float getFloat(String name) {
        return getFloat(name, 0);
    }

    public double getDouble(String name, double def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asDouble();
    }

    public double getDouble(String name) {
        return getDouble(name, 0);
    }

    public boolean getBoolean(String name) {
        JsonNode node = getNode(name);

        return node != null && node.asBoolean();
    }

    @Contract("_, !null -> !null")
    public String getString(@NotNull String name, @Nullable String def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asString();
    }

    @Nullable
    public String getString(@NotNull String name) {
        return getString(name, null);
    }

    @Contract("_, !null -> !null")
    public BigDecimal getBigDecimal(@NotNull String name, @Nullable BigDecimal def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asBigDecimal();
    }

    @Nullable
    public BigDecimal getBigDecimal(@NotNull String name) {
        return getBigDecimal(name, null);
    }

    @Contract("_, !null -> !null")
    public String getEscapedString(@NotNull String name, @Nullable String def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asEscapedString();
    }

    @Nullable
    public String getEscapedString(@NotNull String name) {
        return getEscapedString(name, null);
    }

    @Contract("_, !null -> !null")
    public String getUnescapedString(@NotNull String name, @Nullable String def) {
        JsonNode node = getNode(name);

        return node == null ? def : node.asUnescapedString();
    }

    @Nullable
    public String getUnescapedString(@NotNull String name) {
        return getEscapedString(name, null);
    }

    @Nullable
    public Object getObject(String name) {
        return getOptObject(name).orElse(null);
    }

    @NotNull
    public Optional<Object> getOptObject(String name) {
        JsonNode node = getNode(name);

        return node == null ? Optional.empty() : Optional.of(node.asObject());
    }

    @Nullable
    public JsonArray getArray(String name) {
        JsonNode node = getNode(name);

        return node == null ? null : node.asArray();
    }

    public JsonNode getNode(String name) {
        return _nodes.get(name);
    }

}
