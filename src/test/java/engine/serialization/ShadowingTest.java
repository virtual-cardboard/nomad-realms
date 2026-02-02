package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ShadowingTest {

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
