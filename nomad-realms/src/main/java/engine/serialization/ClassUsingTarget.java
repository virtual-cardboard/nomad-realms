package engine.serialization;

/**
 * Test class that uses TargetClass as a field to verify that the DerializableProcessor
 * correctly identifies and uses the custom derializer for TargetClass.
 */
@Derializable
public class ClassUsingTarget {

	private TargetClass target;

	public ClassUsingTarget() {
	}

	public ClassUsingTarget(TargetClass target) {
		this.target = target;
	}

	public TargetClass getTarget() {
		return target;
	}

}
