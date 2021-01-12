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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Unidentified Person
 */
public class JsonTest {

    Json json;

    @BeforeEach
    void init() {
        json = Json.defaultInstance();
        json.setCacheBufferedValues(false);
        json.setCheckRawValues(false);
        json.setPrettyPrinting(false);
        json.setAutoUnescape(false);
    }

    @Test
    void testPrettyPrintingValues() {
        JsonObject object = new JsonObject();
        object.putEmptyArray("empty_array");
        object.putEmptyObject("empty_object");

        object.put("user", new JsonObject()
                .put("first_name", "Unidentified")
                .put("last_name", "Person")
                .put("age", 20));

        assertEquals("{\"empty_array\":[],\"empty_object\":{},\"user\":{\"first_name\":\"Unidentified\",\"last_name\":\"Person\",\"age\":20}}",
                object.toSimpleJson());

        assertEquals("{\n" +
                "\t\"empty_array\": [ ],\n" +
                "\t\"empty_object\": { },\n" +
                "\t\"user\": {\n" +
                "\t\t\"first_name\": \"Unidentified\",\n" +
                "\t\t\"last_name\": \"Person\",\n" +
                "\t\t\"age\": 20\n" +
                "\t}\n" +
                "}", object.toPrettyJson());

        assertEquals("{\n" +
                " \"empty_array\": [ ],\n" +
                " \"empty_object\": { },\n" +
                " \"user\": {\n" +
                "  \"first_name\": \"Unidentified\",\n" +
                "  \"last_name\": \"Person\",\n" +
                "  \"age\": 20\n" +
                " }\n" +
                "}", object.toPrettyJson(IndentType.ONE_SPACE));
    }

    @Test
    void testCheckRawValuesOnlyValues() {
        JsonNode node = json.fromJson("\"Hello\\u0020world!\"");
        assertEquals(new JsonValue("Hello world!"), node);
        json.setCheckRawValues(true);
        node = json.fromJson("\"Hello\\u0020world!\"");
        assertNotEquals(new JsonValue("Hello world!"), node);
    }

    @Test
    void testCacheBufferedValues() {
        JsonNode node = json.fromJson("\"Hello world!\"");
        assertNotSame(node.asString(), node.asString());
        json.setCacheBufferedValues(true);
        node = json.fromJson("\"Hello world!\"");
        assertSame(node.asString(), node.asString());
    }

    @Test
    void testAutoEscape() {
        assertEquals("Hello\\u0020\\tworld!", json.fromJson("\"Hello\\u0020\\tworld!\"").asString());
        json.setAutoUnescape(true);
        assertEquals("Hello \tworld!", json.fromJson("\"Hello\\u0020\\tworld!\"").asString());
    }

    @Test
    void testKeyword_toJson() {
        assertEquals("true", JsonBoolean.TRUE.toSimpleJson());
        assertEquals("false", JsonBoolean.FALSE.toSimpleJson());
        assertEquals("null", JsonNull.INSTANCE.toSimpleJson());
    }

    @Test
    void testKeyword_fromJson() {
        assertEquals(JsonBoolean.TRUE, json.fromJson("true"));
        assertEquals(JsonBoolean.FALSE, json.fromJson("false"));
        assertEquals(JsonNull.INSTANCE, json.fromJson("null"));
    }

    @Test
    void testArray_toJson() {
        assertEquals("[100,200,300,400,500]", new JsonArray(100, 200, 300, 400, 500).toSimpleJson());
        assertEquals("[\"100\",\"200\",\"300\",\"400\",\"500\"]", new JsonArray("100", "200", "300", "400", "500").toSimpleJson());
    }

    @Test
    void testArray_fromJson() {
        assertEquals(new JsonArray(100, 200, 300, 400, 500), json.fromJson("[100,200,300,400,500]"));
        assertEquals(new JsonArray("100", "200", "300", "400", "500"), json.fromJson("[\"100\",\"200\",\"300\",\"400\",\"500\"]"));
    }

    @Test
    void testObject_toJson() {
        JsonObject object = new JsonObject();
        object.put("message", "Hello world!");
        object.put("message_with_illegal_characters", "\ta\nb\rc\fd\be\u0000f");
        object.putNull("null");
        object.put("code", 200);
        object.put("array", 1, 2, 3);

        String result = "{\"message\":\"Hello world!\",\"message_with_illegal_characters\":\"\\ta\\nb\\rc\\fd\\be\\u0000f\",\"null\":null,\"code\":200,\"array\":[1,2,3]}";

        assertEquals(result, object.toSimpleJson());
    }

    @Test
    void testObject_fromJson() {
        JsonObject object = new JsonObject();
        object.put("message", "Hello world!");
        object.put("message_with_illegal_characters", "\ta\nb\rc\fd\be\u0000f");
        object.putNull("null");
        object.put("code", 200);
        object.put("array", 1, 2, 3);

        String result = "{\"message\":\"Hello world!\",\"message_with_illegal_characters\":\"\\ta\\nb\\rc\\fd\\be\\u0000f\",\"null\":null,\"code\":200,\"array\":[1,2,3]}";

        assertEquals(object, json.fromJson(result));
    }

    @Test
    void testBigDecimal_toJson() {
        assertEquals("0.06455",
                new JsonValue(BigDecimal.valueOf(64.55E-3)).toSimpleJson());
        assertEquals("-0.06455",
                new JsonValue(BigDecimal.valueOf(-64.55E-3)).toSimpleJson());
    }

    @Test
    void testBigDecimal_fromJson() {
        assertEquals(BigDecimal.valueOf(64.55E-3),
                json.fromJson("64.55E-3").asBigDecimal());
        assertEquals(BigDecimal.valueOf(-64.55E-3),
                json.fromJson("-64.55E-3").asBigDecimal());
    }

    @Test
    void testLong_toJson() {
        assertEquals("64",
                new JsonValue(64L).toSimpleJson());
        assertEquals("-64",
                new JsonValue(-64L).toSimpleJson());
    }

    @Test
    void testLong_fromJson() {
        assertEquals(64,
                json.fromJson("64").asLong());
        assertEquals(-64,
                json.fromJson("-64").asLong());
        assertEquals(100,
                json.fromJson("1E2").asLong());
    }

    @Test
    void testShort_toJson() {
        assertEquals("64",
                new JsonValue((short) 64).toSimpleJson());
        assertEquals("-64",
                new JsonValue((short) -64).toSimpleJson());
    }

    @Test
    void testShort_fromJson() {
        assertEquals(64,
                json.fromJson("64").asShort());
        assertEquals(-64,
                json.fromJson("-64").asShort());
        assertEquals(100,
                json.fromJson("1E2").asShort());
    }

    @Test
    void testByte_toJson() {
        assertEquals("64",
                new JsonValue((byte) 64).toSimpleJson());
        assertEquals("-64",
                new JsonValue((byte) -64).toSimpleJson());
    }

    @Test
    void testByte_fromJson() {
        assertEquals(64,
                json.fromJson("64").asByte());
        assertEquals(-64,
                json.fromJson("-64").asByte());
        assertEquals(100,
                json.fromJson("1E2").asByte());
    }

    @Test
    void testDouble_toJson() {
        assertEquals("64.5",
                new JsonValue(64.5).toSimpleJson());
        assertEquals("-64.5",
                new JsonValue(-64.5).toSimpleJson());
    }

    @Test
    void testDouble_fromJson() {
        assertEquals(64.5,
                json.fromJson("64.5").asDouble());
        assertEquals(-64.5,
                json.fromJson("-64.5").asDouble());
    }

    @Test
    void testFloat_toJson() {
        assertEquals("64.5",
                new JsonValue(64.5F).toSimpleJson());
        assertEquals("-64.5",
                new JsonValue(-64.5F).toSimpleJson());
    }

    @Test
    void testFloat_fromJson() {
        assertEquals(64.5,
                json.fromJson("64.5").asFloat());
        assertEquals(-64.5,
                json.fromJson("-64.5").asFloat());
        assertEquals(1500,
                json.fromJson("1.5E3").asFloat());
    }

    @Test
    void testInt_toJson() {
        assertEquals("64",
                new JsonValue(64).toSimpleJson());
        assertEquals("-64",
                new JsonValue(-64).toSimpleJson());
    }

    @Test
    void testInt_fromJson() {
        assertEquals(64,
                json.fromJson("64").asInt());
        assertEquals(-64,
                json.fromJson("-64").asInt());
        assertEquals(1000,
                json.fromJson("1E3").asInt());
    }

    @Test
    void testString_toJson() {
        assertEquals("\"Hello world!\"",
                new JsonValue("Hello world!").toSimpleJson());
        assertEquals("\"New line\\nNew line\"",
                new JsonValue("New line\nNew line").toSimpleJson());
        assertEquals("\"\\tTab\\\"quote\\/slash\\\\backslash\"",
                new JsonValue("\tTab\"quote/slash\\backslash").toSimpleJson());
    }

    @Test
    void testString_fromJson() {
        assertEquals("Hello world!",
                json.fromJson("\"Hello world!\"").asString());
        assertEquals("New line\nNew line",
                json.fromJson("\"New line\\nNew line\"").asUnescapedString());
        assertEquals("\tTab\"quote/slash\\backslash",
                json.fromJson("\"\\tTab\\\"quote\\/slash\\\\backslash\"").asUnescapedString());
        assertEquals("New line\\nNew line",
                json.fromJson("\"New line\\nNew line\"").asEscapedString());
        assertEquals("\\tTab\\\"quote\\/slash\\\\backslash",
                json.fromJson("\"\\tTab\\\"quote\\/slash\\\\backslash\"").asEscapedString());
    }

}
