package engine.serialization;

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
