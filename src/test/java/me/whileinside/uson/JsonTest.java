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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

// todo option test
// todo stream test
/**
 * @author Unidentified Person
 */
public class JsonTest {

    Json json;

    @BeforeEach
    void init() {
        json = Json.defaultInstance();
    }

    @Test
    void testKeyword_toJson() {
        assertEquals("true", json.toJson(JsonBoolean.TRUE));
        assertEquals("false", json.toJson(JsonBoolean.FALSE));
        assertEquals("null", json.toJson(JsonNull.INSTANCE));
    }

    @Test
    void testKeyword_fromJson() {
        assertEquals(JsonBoolean.TRUE, json.fromJson("true"));
        assertEquals(JsonBoolean.FALSE, json.fromJson("false"));
        assertEquals(JsonNull.INSTANCE, json.fromJson("null"));
    }

    @Test
    void testArray_toJson() {
        assertEquals("[100,200,300,400,500]", json.toJson(new JsonArray(100, 200, 300, 400, 500)));
        assertEquals("[\"100\",\"200\",\"300\",\"400\",\"500\"]", json.toJson(new JsonArray("100", "200", "300", "400", "500")));
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

        assertEquals(result, json.toJson(object));
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
                json.toJson(new JsonValue(BigDecimal.valueOf(64.55E-3))));

        assertEquals("-0.06455",
                json.toJson(new JsonValue(BigDecimal.valueOf(-64.55E-3))));
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
                json.toJson(new JsonValue(64L)));

        assertEquals("-64",
                json.toJson(new JsonValue(-64L)));
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
                json.toJson(new JsonValue((short) 64)));

        assertEquals("-64",
                json.toJson(new JsonValue((short) -64)));
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
                json.toJson(new JsonValue((byte) 64)));

        assertEquals("-64",
                json.toJson(new JsonValue((byte) -64)));
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
                json.toJson(new JsonValue(64.5)));

        assertEquals("-64.5",
                json.toJson(new JsonValue(-64.5)));
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
                json.toJson(new JsonValue(64.5F)));

        assertEquals("-64.5",
                json.toJson(new JsonValue(-64.5F)));
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
                json.toJson(new JsonValue(64)));

        assertEquals("-64",
                json.toJson(new JsonValue(-64)));
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
                json.toJson(new JsonValue("Hello world!")));

        assertEquals("\"New line\\nNew line\"",
                json.toJson(new JsonValue("New line\nNew line")));

        assertEquals("\"\\tTab\\\"quote\\/slash\\\\backslash\"",
                json.toJson(new JsonValue("\tTab\"quote/slash\\backslash")));
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
