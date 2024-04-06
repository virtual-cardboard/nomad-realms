package nomadrealms.game.card.target;

public class TargetingInfo {

	private TargetType targetType;
	private int range;

	public TargetingInfo(TargetType targetType, int range) {
		this.targetType = targetType;
		this.range = range;
	}

	public TargetType targetType() {
		return targetType;
	}

	public int range() {
		return range;
	}

}
