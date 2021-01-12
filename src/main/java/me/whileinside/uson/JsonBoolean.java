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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Boolean[" + value + "]";
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull CharSequence asRaw() {
        return asString();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isBoolean() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull String asString() {
        return Boolean.toString(value);
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull String asEscapedString() {
        return asString();
    }

    /** {@inheritDoc} */
    @Override
    public boolean asBoolean() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public void toSimpleJson(Appendable appendable) throws IOException {
        appendable.append(asRaw());
    }

    /** {@inheritDoc} */
    @Override
    void toPrettyJson(Appendable appendable, IndentType indentType, int tabs) throws IOException {
        toSimpleJson(appendable);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof JsonBoolean)) return false;

        return value == ((JsonBoolean) obj).value;
    }
}
