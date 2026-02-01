package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class DerializableTest {

    @Test
    public void testInheritanceSerialization() {
        SubClass original = new SubClass();
        original.setSuperField(10);
        original.setSubField(20);

        byte[] bytes = SubClassDerializer.serialize(original);
        SubClass deserialized = SubClassDerializer.deserialize(bytes);

        assertNotNull(deserialized);
        assertEquals(20, deserialized.getSubField(), "Subclass field should be preserved");
        assertEquals(10, deserialized.getSuperField(), "Superclass field should be preserved");
    }
}
