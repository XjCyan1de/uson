package me.whileinside.uson;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Unidentified Person
 */
public class JsonTest {

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
