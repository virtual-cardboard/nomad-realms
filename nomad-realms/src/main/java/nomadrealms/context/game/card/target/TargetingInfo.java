package nomadrealms.context.game.card.target;

import java.util.Arrays;
import java.util.List;

import nomadrealms.context.game.card.condition.Condition;

public class TargetingInfo {

	private final TargetType targetType;
	private final List<Condition> conditions;

	public TargetingInfo(TargetType targetType, Condition... conditions) {
		this.targetType = targetType;
		this.conditions = Arrays.asList(conditions);
	}

	public TargetType targetType() {
		return targetType;
	}

	public List<Condition> conditions() {
		return conditions;
	}

}
