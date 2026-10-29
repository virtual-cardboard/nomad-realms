package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testCollections() {
        CollectionTestClass original = new CollectionTestClass();

        List<String> stringList = Arrays.asList("hello", "world");
        original.setStringList(stringList);

        ShadowChild c1 = new ShadowChild();
        c1.setParentVal(10);
        c1.setChildVal(20);

        ShadowChild c2 = new ShadowChild();
        c2.setParentVal(30);
        c2.setChildVal(40);

        original.setChildList(Arrays.asList(c1, c2));

        Map<String, Integer> stringIntMap = new HashMap<>();
        stringIntMap.put("one", 1);
        stringIntMap.put("two", 2);
        original.setStringIntMap(stringIntMap);

        Map<String, ShadowChild> stringChildMap = new HashMap<>();
        stringChildMap.put("first", c1);
        stringChildMap.put("second", c2);
        original.setStringChildMap(stringChildMap);

        byte[] bytes = CollectionTestClassDerializer.serialize(original);
        CollectionTestClass deserialized = CollectionTestClassDerializer.deserialize(bytes);

        assertNotNull(deserialized);
        assertEquals(stringList, deserialized.getStringList());

        assertNotNull(deserialized.getChildList());
        assertEquals(2, deserialized.getChildList().size());
        assertEquals(10, deserialized.getChildList().get(0).getParentVal());
        assertEquals(20, deserialized.getChildList().get(0).getChildVal());
        assertEquals(30, deserialized.getChildList().get(1).getParentVal());
        assertEquals(40, deserialized.getChildList().get(1).getChildVal());

        assertEquals(stringIntMap, deserialized.getStringIntMap());

        assertNotNull(deserialized.getStringChildMap());
        assertEquals(2, deserialized.getStringChildMap().size());
        assertEquals(10, deserialized.getStringChildMap().get("first").getParentVal());
        assertEquals(20, deserialized.getStringChildMap().get("first").getChildVal());
        assertEquals(30, deserialized.getStringChildMap().get("second").getParentVal());
        assertEquals(40, deserialized.getStringChildMap().get("second").getChildVal());
    }

    @Test
    public void testPolymorphicListRoundTrip() {
        // Given a list of abstract @Derializable elements with mixed subclasses and a null entry
        SubAClass subA = new SubAClass();
        subA.setSuperField(10);
        subA.setSubAField(20);

        SubBClass subB = new SubBClass();
        subB.setSuperField(30);
        subB.setSubBField(40L);

        PolymorphicListTestClass original = new PolymorphicListTestClass();
        original.setItems(Arrays.asList(subA, null, subB));

        Map<String, SuperClass> itemByName = new HashMap<>();
        itemByName.put("a", subA);
        itemByName.put("b", subB);
        original.setItemByName(itemByName);

        // When serializing and deserializing
        byte[] bytes = PolymorphicListTestClassDerializer.serialize(original);
        PolymorphicListTestClass deserialized = PolymorphicListTestClassDerializer.deserialize(bytes);

        // Then the subclass types, null entry, and field values should round trip
        assertNotNull(deserialized);
        List<SuperClass> items = deserialized.getItems();
        assertEquals(3, items.size());
        assertTrue(items.get(0) instanceof SubAClass, "SubAClass should deserialize as SubAClass");
        assertEquals(10, items.get(0).getSuperField());
        assertEquals(20, ((SubAClass) items.get(0)).getSubAField());
        assertNull(items.get(1), "null entry should stay null");
        assertTrue(items.get(2) instanceof SubBClass, "SubBClass should deserialize as SubBClass");
        assertEquals(30, items.get(2).getSuperField());
        assertEquals(40L, ((SubBClass) items.get(2)).getSubBField());

        assertNotNull(deserialized.getItemByName());
        assertEquals(2, deserialized.getItemByName().size());
        assertTrue(deserialized.getItemByName().get("a") instanceof SubAClass);
        assertEquals(20, ((SubAClass) deserialized.getItemByName().get("a")).getSubAField());
        assertTrue(deserialized.getItemByName().get("b") instanceof SubBClass);
        assertEquals(40L, ((SubBClass) deserialized.getItemByName().get("b")).getSubBField());
    }

    @Test
    public void testNullListAndMapRoundTrip() {
        // Given a class with a null list and null map
        PolymorphicListTestClass original = new PolymorphicListTestClass();

        // When serializing and deserializing
        byte[] bytes = PolymorphicListTestClassDerializer.serialize(original);
        PolymorphicListTestClass deserialized = PolymorphicListTestClassDerializer.deserialize(bytes);

        // Then both should stay null
        assertNotNull(deserialized);
        assertNull(deserialized.getItems());
        assertNull(deserialized.getItemByName());
    }
}
