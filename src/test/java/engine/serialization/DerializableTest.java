package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DerializableTest {

    @Test
    public void testInheritanceSerialization() {
        SubAClass original = new SubAClass();
        original.setSuperField(10);
        original.setSubAField(20);

        byte[] bytes = SubAClassDerializer.serialize(original);
        SubAClass deserialized = SubAClassDerializer.deserialize(bytes);

        assertNotNull(deserialized);
        assertEquals(20, deserialized.getSubAField(), "Subclass field should be preserved");
        assertEquals(10, deserialized.getSuperField(), "Superclass field should be preserved");
    }

    @Test
    public void testSimplifiedFieldAccess() {
        SubAClass sub = new SubAClass();
        sub.setSuperField(100);
        sub.setSubAField(200);

        // Test getField
        assertEquals(100, DerializableHelper.getField(sub, "superField"));
        assertEquals(200, DerializableHelper.getField(sub, "subAField"));

        // Test setField
        DerializableHelper.setField(sub, "superField", 300);
        DerializableHelper.setField(sub, "subAField", 400);

        assertEquals(300, sub.getSuperField());
        assertEquals(400, sub.getSubAField());
    }

    @Test
    public void testAbstractClassPolymorphism() {
        SuperClass subA = new SubAClass();
        subA.setSuperField(10);
        ((SubAClass) subA).setSubAField(20);

        SuperClass subB = new SubBClass();
        subB.setSuperField(30);
        ((SubBClass) subB).setSubBField(40L);

        // Serialize SubA as SuperClass
        byte[] bytesA = SuperClassDerializer.serialize(subA);
        SuperClass deserializedA = SuperClassDerializer.deserialize(bytesA);

        assertTrue(deserializedA instanceof SubAClass);
        assertEquals(10, deserializedA.getSuperField());
        assertEquals(20, ((SubAClass) deserializedA).getSubAField());

        // Serialize SubB as SuperClass
        byte[] bytesB = SuperClassDerializer.serialize(subB);
        SuperClass deserializedB = SuperClassDerializer.deserialize(bytesB);

        assertTrue(deserializedB instanceof SubBClass);
        assertEquals(30, deserializedB.getSuperField());
        assertEquals(40L, ((SubBClass) deserializedB).getSubBField());
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
