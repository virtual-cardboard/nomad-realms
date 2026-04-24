package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class CustomDerializerTest {

	@Test
	public void testCustomDerializer() {
		TargetClass target = new TargetClass("banana");
		ClassUsingTarget original = new ClassUsingTarget(target);

		byte[] bytes = ClassUsingTargetDerializer.serialize(original);

		// Verify custom serialization logic (integer representation)
		// ClassUsingTargetDerializer will write boolean(true) then call TargetClassDerializer.serialize
		// TargetClassDerializer writes an int(2) for "banana"
		DataInputStream disRaw = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			disRaw.readBoolean(); // target != null
			int fruitId = disRaw.readInt();
			assertEquals(2, fruitId, "Fruit 'banana' should be serialized as integer 2");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ClassUsingTarget deserialized = ClassUsingTargetDerializer.deserialize(bytes);

		assertNotNull(deserialized);
		assertNotNull(deserialized.getTarget());
		assertEquals("banana", deserialized.getTarget().getFruit());
	}

}
