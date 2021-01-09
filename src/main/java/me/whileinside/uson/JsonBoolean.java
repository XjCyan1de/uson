package me.whileinside.uson;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * @author Unidentified Person
 */
public class JsonBoolean extends JsonNode {

    public static final JsonBoolean
            TRUE = new JsonBoolean(true),
            FALSE = new JsonBoolean(false);

    private final boolean value;

    @ApiStatus.Internal
    JsonBoolean(boolean value) {
        this.value = value;
    }

    public static JsonBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return "Boolean[" + value + "]";
    }

    @Override
    public @NotNull CharSequence asRaw() {
        return asString();
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public @NotNull String asString() {
        return Boolean.toString(value);
    }

    @Override
    public @NotNull String asEscapedString() {
        return asString();
    }

    @Override
    public boolean asBoolean() {
        return value;
    }
}
