package me.whileinside.uson;

import org.jetbrains.annotations.ApiStatus;

/**
 * @author Unidentified Person
 */
public class JsonNull extends JsonNode {

    public static final JsonNull INSTANCE = new JsonNull();

    @ApiStatus.Internal
    JsonNull() {
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return "Null";
    }
}
