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

    /** check if node is an array */
    public boolean isArray() {
        return false;
    }

    /** check if node is an object */
    public boolean isObject() {
        return false;
    }

    /** check if node is a value */
    public boolean isValue() {
        return false;
    }

    /** check if node is a number */
    public boolean isNumber() {
        return false;
    }

    /** check if node is a string */
    public boolean isString() {
        return false;
    }

    /** check if node is a null */
    public boolean isNull() {
        return false;
    }

    /** check if node is a boolean */
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
        throw new ClassCastException();
    }

    /** cast node as escaped string */
    public @NotNull String asEscapedString() {
        throw new ClassCastException();
    }

    public @NotNull String asUnescapedString() {
        throw new ClassCastException();
    }

    /** cast node as byte */
    public byte asByte() {
        throw new ClassCastException();
    }

    /** cast node as short */
    public short asShort() {
        throw new ClassCastException();
    }

    /** cast node as int */
    public int asInt() {
        throw new ClassCastException();
    }

    /** cast node as long */
    public long asLong() {
        throw new ClassCastException();
    }

    /** cast node as float */
    public float asFloat() {
        throw new ClassCastException();
    }

    /** cast node as double */
    public double asDouble() {
        throw new ClassCastException();
    }

    /** cast node as boolean */
    public boolean asBoolean() {
        throw new ClassCastException();
    }

    /** cast node as big decimal */
    public @NotNull BigDecimal asBigDecimal() {
        throw new ClassCastException();
    }

    /** write node as json without any indentations */
    public String toSimpleJson() {
        StringBuilder sb = new StringBuilder();

        try {
            toSimpleJson(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /** write node as json with indentations (1 tab) */
    public String toPrettyJson() {
        return toPrettyJson(IndentType.ONE_TAB);
    }

    /** write node as json with indentations */
    public String toPrettyJson(IndentType indentType) {
        StringBuilder sb = new StringBuilder();

        try {
            toPrettyJson(sb, indentType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    /** write node as json with indentations to specified output */
    public void toPrettyJson(Appendable appendable, IndentType indentType) throws IOException {
        toPrettyJson(appendable, indentType, 1);
    }

    /** write node as json without any indentations to specified output */
    public abstract void toSimpleJson(Appendable appendable) throws IOException;

    /** write node as json with indentations to specified output */
    abstract void toPrettyJson(Appendable appendable, IndentType indentType, int tabs) throws IOException;

}
