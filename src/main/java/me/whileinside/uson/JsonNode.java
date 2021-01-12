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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Unidentified Person
 */
public abstract class JsonNode {

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isValue() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public @NotNull CharSequence asRaw() {
        throw new UnsupportedOperationException();
    }

    /** cast node as array */
    public @NotNull JsonArray asArray() {
        throw new ClassCastException();
    }

    /** cast node as value */
    public @NotNull JsonValue asValue() {
        throw new ClassCastException();
    }

    /** cast node as object */
    public @NotNull JsonObject asObject() {
        throw new ClassCastException();
    }

    /** cast node as string */
    public @NotNull String asString() {
        throw new UnsupportedOperationException();
    }

    /** cast node as escaped string */
    public @NotNull String asEscapedString() {
        throw new UnsupportedOperationException();
    }
    public @NotNull String asUnescapedString() {
        throw new UnsupportedOperationException();
    }

    /** cast node as byte */
    public byte asByte() {
        throw new UnsupportedOperationException();
    }

    /** cast node as short */
    public short asShort() {
        throw new UnsupportedOperationException();
    }

    /** cast node as int */
    public int asInt() {
        throw new UnsupportedOperationException();
    }

    /** cast node as long */
    public long asLong() {
        throw new UnsupportedOperationException();
    }

    /** cast node as float */
    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    /** cast node as double */
    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    /** cast node as boolean */
    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    /** cast node as big decimal */
    public @NotNull BigDecimal asBigDecimal() {
        throw new UnsupportedOperationException();
    }

    public String toSimpleJson() {
        StringBuilder sb = new StringBuilder();

        try {
            toSimpleJson(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public String toPrettyJson() {
        return toPrettyJson(IndentType.ONE_TAB);
    }

    public String toPrettyJson(IndentType indentType) {
        StringBuilder sb = new StringBuilder();

        try {
            toPrettyJson(sb, indentType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public void toPrettyJson(Appendable appendable, IndentType indentType) throws IOException {
        toPrettyJson(appendable, indentType, 1);
    }

    public abstract void toSimpleJson(Appendable appendable) throws IOException;
    public abstract void toPrettyJson(Appendable appendable, IndentType indentType, int tabs) throws IOException;

}
