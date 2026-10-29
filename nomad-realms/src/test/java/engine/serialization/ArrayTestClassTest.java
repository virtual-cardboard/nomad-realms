package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class ArrayTestClassTest {

    @Test
    public void test2DPrimitiveArrayRoundTrip() {
        // Given a class holding a 2D primitive array with a null row
        ArrayTestClass original = new ArrayTestClass();
        original.setIntGrid(new int[][] { {1, 2}, null, {3, 4, 5} });

        // When serializing and deserializing
        byte[] bytes = ArrayTestClassDerializer.serialize(original);
        ArrayTestClass deserialized = ArrayTestClassDerializer.deserialize(bytes);

        // Then the array structure and contents should round trip, including the null row
        assertNotNull(deserialized);
        assertTrue(Arrays.deepEquals(original.getIntGrid(), deserialized.getIntGrid()), "2D int grid should round trip");
    }

    @Test
    public void test2DDerializableArrayWithNulls() {
        // Given a class holding a 2D array of @Derializable objects with null rows and null entries
        ShadowChild c1 = new ShadowChild();
        c1.setParentVal(10);
        c1.setChildVal(20);

        ShadowChild c2 = new ShadowChild();
        c2.setParentVal(30);
        c2.setChildVal(40);

        ShadowChild c3 = new ShadowChild();
        c3.setParentVal(50);
        c3.setChildVal(60);

        ArrayTestClass original = new ArrayTestClass();
        original.setChildren(new ShadowChild[][] { {c1, null}, null, {c2, c3} });

        // When serializing and deserializing
        byte[] bytes = ArrayTestClassDerializer.serialize(original);
        ArrayTestClass deserialized = ArrayTestClassDerializer.deserialize(bytes);

        // Then the null rows and null entries should be preserved, alongside the field values
        assertNotNull(deserialized);
        ShadowChild[][] loadedChildren = deserialized.getChildren();
        assertNotNull(loadedChildren);
        assertEquals(3, loadedChildren.length);
        assertNull(loadedChildren[1], "null row should stay null");
        assertEquals(2, loadedChildren[0].length);
        assertNull(loadedChildren[0][1], "null entry should stay null");
        assertEquals(10, loadedChildren[0][0].getParentVal());
        assertEquals(20, loadedChildren[0][0].getChildVal());
        assertEquals(30, loadedChildren[2][0].getParentVal());
        assertEquals(40, loadedChildren[2][0].getChildVal());
        assertEquals(50, loadedChildren[2][1].getParentVal());
        assertEquals(60, loadedChildren[2][1].getChildVal());
    }

    @Test
    public void test1DPrimitiveAndStringArrayRoundTrip() {
        // Given a class holding 1D arrays
        ArrayTestClass original = new ArrayTestClass();
        original.setNames(new String[] {"alpha", "beta", "gamma"});

        // When serializing and deserializing
        byte[] bytes = ArrayTestClassDerializer.serialize(original);
        ArrayTestClass deserialized = ArrayTestClassDerializer.deserialize(bytes);

        // Then the arrays should round trip
        assertNotNull(deserialized);
        assertArrayEquals(original.getNames(), deserialized.getNames(), "String array should round trip");
    }

}
