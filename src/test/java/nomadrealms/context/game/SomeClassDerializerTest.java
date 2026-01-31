package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SomeClassDerializerTest {

    @Test
    public void testSerialization() {
        SomeClass original = new SomeClass("Test Name", 123);
        SomeClassDerializer serializer = new SomeClassDerializer();
        byte[] bytes = serializer.serialize(original);
        assertNotNull(bytes);

        SomeClass deserialized = serializer.deserialize(bytes);
        assertNotNull(deserialized);
        assertEquals(original.getName(), deserialized.getName());
        assertEquals(original.getValue(), deserialized.getValue());
    }

}
