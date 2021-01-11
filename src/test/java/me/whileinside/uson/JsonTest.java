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

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Unidentified Person
 */
public class JsonTest {

    @Test
    public void testUnescape() {
        assertEquals("Привет мир!", Json.unescape("Привет мир!"));
        assertEquals("\u0000 is NULL", Json.unescape("\\u0000 is NULL"));
        assertEquals("\n is NEWLINE", Json.unescape("\\n is NEWLINE"));
        assertEquals("\t is TAB", Json.unescape("\\t is TAB"));
        assertEquals("\b is BACKSPACE", Json.unescape("\\b is BACKSPACE"));
        assertEquals("\r is CARRIAGE RETURN", Json.unescape("\\r is CARRIAGE RETURN"));
        assertEquals("\f is FORMFEED", Json.unescape("\\f is FORMFEED"));
        assertEquals("\" is DOUBLE QUOTE", Json.unescape("\\\" is DOUBLE QUOTE"));
    }

    @Test
    public void testEscape() {
        assertEquals("Привет мир!", Json.escape("Привет мир!"));
        assertEquals("\\u0000 is NULL", Json.escape("\u0000 is NULL"));
        assertEquals("\\n is NEWLINE", Json.escape("\n is NEWLINE"));
        assertEquals("\\t is TAB", Json.escape("\t is TAB"));
        assertEquals("\\b is BACKSPACE", Json.escape("\b is BACKSPACE"));
        assertEquals("\\r is CARRIAGE RETURN", Json.escape("\r is CARRIAGE RETURN"));
        assertEquals("\\f is FORMFEED", Json.escape("\f is FORMFEED"));
        assertEquals("\\\" is DOUBLE QUOTE", Json.escape("\" is DOUBLE QUOTE"));
    }

    @Test
    public void testStream() throws IOException {
        Json json = Json.defaultInstance();
        json.addOptions(Json.AUTO_UNESCAPE);

        String jsonValue = "{\"message\": {\"en\": \"Hello world!\", \"ru\": \"\\u041F\\u0440\\u0438\\u0432\\u0435\\u0442 \\u043C\\u0438\\u0440!\"}}";

        JsonNode node = json.fromJson(new StringReader(jsonValue));
        assertTrue(node.isObject());
        assertEquals("Hello world!", node.asObject().getObject("message").getString("en"));
        assertEquals("Привет мир!", node.asObject().getObject("message").getString("ru"));
    }

    @Test
    public void testExp() {
        Json json = Json.defaultInstance();

        { // float
            assertEquals(0.0123123f, json.fromJson("123.123E-4").asFloat());
        }

        { // double
            assertEquals(0.0123123, json.fromJson("123.123E-4").asDouble());
        }

        { // long
            assertEquals(12L, json.fromJson("123E-1").asLong());
        }

    }

    @Test
    public void testToJson() {
        Json json = Json.defaultInstance();

        assertEquals("\"Hello world!\"",
                json.toJson(new JsonValue("Hello world!")));

        assertEquals("12345.3215678",
                json.toJson(new JsonValue(12345.3215678)));

        assertEquals("[\"Hello\",\" \",\"world!\"]",
                json.toJson(new JsonArray(new JsonValue("Hello"), new JsonValue(" "), new JsonValue("world!"))));

        assertEquals("null", json.toJson(JsonNull.INSTANCE));
        assertEquals("true", json.toJson(JsonBoolean.TRUE));
        assertEquals("false", json.toJson(JsonBoolean.FALSE));
    }

    @Test
    public void testFromJson() {
        Json json = Json.defaultInstance();

        JsonNode stringNode = json.fromJson("\"Hello world!\"");

        assertTrue(stringNode.isString());
        assertEquals("Hello world!", stringNode.asString());

        JsonNode decimalNode = json.fromJson("12345.3215678");
        assertTrue(decimalNode.isNumber());
        assertEquals(12345.3215678, decimalNode.asDouble());

        JsonNode arrayNode = json.fromJson("[\"Hello\", \" \", \"world!\"]");
        assertTrue(arrayNode.isArray());
        assertEquals(3, arrayNode.asArray().count());

        Iterator<JsonNode> arrayNodeIterator = arrayNode.asArray().nodeIterator();
        assertEquals("Hello", arrayNodeIterator.next().asString());
        assertEquals(" ", arrayNodeIterator.next().asString());
        assertEquals("world!", arrayNodeIterator.next().asString());

        JsonNode nullNode = json.fromJson("null");
        assertTrue(nullNode.isNull());

        JsonNode trueNode = json.fromJson("true");
        assertTrue(trueNode.isBoolean());
        assertTrue(trueNode.asBoolean());

        JsonNode falseNode = json.fromJson("false");
        assertTrue(falseNode.isBoolean());
        assertFalse(falseNode.asBoolean());
    }

}
