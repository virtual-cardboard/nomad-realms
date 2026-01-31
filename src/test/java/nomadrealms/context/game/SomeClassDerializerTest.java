package nomadrealms.context.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class SomeClassDerializerTest {

    @Test
    public void testSerialization() {
        NestedClass nested = new NestedClass(456, "Nested Description");
        SomeClass original = new SomeClass("Test Name", 123, 9876543210L, true, nested);

        byte[] bytes = SomeClassDerializer.serialize(original);
        assertNotNull(bytes);

        SomeClass deserialized = SomeClassDerializer.deserialize(bytes);
        assertNotNull(deserialized);
        assertEquals(original.getName(), deserialized.getName());
        assertEquals(original.getValue(), deserialized.getValue());
        assertEquals(original.getTimestamp(), deserialized.getTimestamp());
        assertEquals(original.isActive(), deserialized.isActive());

        assertNotNull(deserialized.getNested());
        assertEquals(original.getNested().getId(), deserialized.getNested().getId());
        assertEquals(original.getNested().getDescription(), deserialized.getNested().getDescription());
    }

    @Test
    public void testSerializationWithNulls() {
        SomeClass original = new SomeClass(null, 0, 0L, false, null);

        byte[] bytes = SomeClassDerializer.serialize(original);
        assertNotNull(bytes);

        SomeClass deserialized = SomeClassDerializer.deserialize(bytes);
        assertNotNull(deserialized);
        assertNull(deserialized.getName());
        assertNull(deserialized.getNested());
    }

}
