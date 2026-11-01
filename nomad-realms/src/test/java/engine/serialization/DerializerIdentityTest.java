package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests object identity tracking: nodes in a cyclic or aliased graph round trip into one shared instance
 * each, instead of recursively duplicating like the old by-value serialization.
 */
public class DerializerIdentityTest {

    @Test
    public void testSelfCycleRoundTrip() {
        DerializerIdentityNode a = new DerializerIdentityNode("a");
        a.partner = a; // a cycle that would recurse forever under the old by-value encoding

        byte[] bytes = DerializerIdentityNodeDerializer.serialize(a);
        DerializerIdentityNode loaded = DerializerIdentityNodeDerializer.deserialize(bytes);

        assertEquals("a", loaded.name);
        assertSame(loaded, loaded.partner);
    }

    @Test
    public void testTwoNodeCycleRoundTrip() {
        DerializerIdentityNode a = new DerializerIdentityNode("a");
        DerializerIdentityNode b = new DerializerIdentityNode("b");
        a.partner = b;
        b.partner = a;

        byte[] bytes = DerializerIdentityNodeDerializer.serialize(a);
        DerializerIdentityNode loaded = DerializerIdentityNodeDerializer.deserialize(bytes);

        assertEquals("b", loaded.partner.name);
        assertSame(loaded, loaded.partner.partner);
        assertSame(loaded.partner, loaded.partner.partner.partner);
    }

    @Test
    public void testAliasAndNullChildrenRoundTrip() {
        DerializerIdentityNode a = new DerializerIdentityNode("a");
        DerializerIdentityNode b = new DerializerIdentityNode("b");
        List<DerializerIdentityNode> repeated = Arrays.asList(a, null, b, a);
        a.children = new ArrayList<>(repeated);

        byte[] bytes = DerializerIdentityNodeDerializer.serialize(a);
        DerializerIdentityNode loaded = DerializerIdentityNodeDerializer.deserialize(bytes);

        List<DerializerIdentityNode> loadedChildren = loaded.children;
        assertEquals(4, loadedChildren.size());
        assertNull(loadedChildren.get(1));
        assertSame(loaded, loadedChildren.get(0));
        assertSame(loadedChildren.get(0), loadedChildren.get(3));
        assertEquals("b", loadedChildren.get(2).name);
    }

    @Test
    public void testSerializeTwiceIndependently() {
        DerializerIdentityNode a = new DerializerIdentityNode("a");
        a.partner = a;
        for (int round = 0; round < 2; round++) {
            // Each serialize call gets its own identity namespace; a leaked context would emit a
            // back-reference-only second stream and deserialize into an empty body.
            byte[] bytes = DerializerIdentityNodeDerializer.serialize(a);
            DerializerIdentityNode loaded = DerializerIdentityNodeDerializer.deserialize(bytes);
            assertEquals("a", loaded.name);
            assertSame(loaded, loaded.partner);
        }
    }

}
