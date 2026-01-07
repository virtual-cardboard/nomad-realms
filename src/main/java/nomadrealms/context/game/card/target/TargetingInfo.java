package nomadrealms.context.game.card.target;

import java.util.Arrays;
import java.util.List;

import nomadrealms.context.game.card.condition.Condition;

public class TargetingInfo {

	private TargetType targetType;
	private int range;
	private List<Condition> conditions;

	public TargetingInfo(TargetType targetType, int range, Condition... conditions) {
		this.targetType = targetType;
		this.range = range;
		this.conditions = Arrays.asList(conditions);
	}

	public TargetType targetType() {
		return targetType;
	}

	public int range() {
		return range;
	}

	public List<Condition> conditions() {
		return conditions;
	}

}
