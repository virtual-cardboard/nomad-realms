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

    @Test
    public void testSimplifiedFieldAccess() {
        SubClass sub = new SubClass();
        sub.setSuperField(100);
        sub.setSubField(200);

        // Test getField
        assertEquals(100, DerializableHelper.getField(sub, "superField"));
        assertEquals(200, DerializableHelper.getField(sub, "subField"));

        // Test setField
        DerializableHelper.setField(sub, "superField", 300);
        DerializableHelper.setField(sub, "subField", 400);

        assertEquals(300, sub.getSuperField());
        assertEquals(400, sub.getSubField());
    }

    @Test
    public void testShadowingSerialization() {
        ShadowChild original = new ShadowChild();
        original.setParentVal(100);
        original.setChildVal(200);

        byte[] bytes = ShadowChildDerializer.serialize(original);
        ShadowChild deserialized = ShadowChildDerializer.deserialize(bytes);

        assertNotNull(deserialized);
        assertEquals(100, deserialized.getParentVal(), "Parent field (shadowed) should be preserved");
        assertEquals(200, deserialized.getChildVal(), "Child field (shadowing) should be preserved");
    }
}
