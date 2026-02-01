package nomadrealms.context.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SomeClassDerializerTest {

    @Test
    public void testSerialization() {
        FieldClass nested = new FieldClass(456, "Nested Description");
        SomeClass original = new SomeClass("Test Name", 123, 9876543210L, true, nested);

        byte[] bytes = SomeClassDerializer.INSTANCE.serialize(original);
        assertNotNull(bytes);

        SomeClass deserialized = SomeClassDerializer.INSTANCE.deserialize(bytes);
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

        byte[] bytes = SomeClassDerializer.INSTANCE.serialize(original);
        assertNotNull(bytes);

        SomeClass deserialized = SomeClassDerializer.INSTANCE.deserialize(bytes);
        assertNotNull(deserialized);
        assertNull(deserialized.getName());
        assertNull(deserialized.getNested());
    }

}
