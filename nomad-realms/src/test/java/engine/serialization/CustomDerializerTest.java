package engine.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class CustomDerializerTest {

	@Test
	public void testCustomDerializer() {
		TargetClass target = new TargetClass("custom-value");
		ClassUsingTarget original = new ClassUsingTarget(target);

		byte[] bytes = ClassUsingTargetDerializer.serialize(original);
		ClassUsingTarget deserialized = ClassUsingTargetDerializer.deserialize(bytes);

		assertNotNull(deserialized);
		assertNotNull(deserialized.getTarget());
		assertEquals("custom-value", deserialized.getTarget().getName());
	}

}
