package model.hidden.objective.decomposition;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.hidden.objective.ObjectiveType;

public class ObjectiveDecompositionRulebook {

	protected Map<ObjectiveType, List<ObjectiveDecompositionRule>> map;

	public ObjectiveDecompositionRulebook(ObjectiveDecompositionRule... rules) {
		map = convertRulesArrayToMap(rules);
	}

	public static ObjectiveDecompositionRulebook decompositionRulebook(ObjectiveDecompositionRule... rules) {
		return new ObjectiveDecompositionRulebook(rules);
	}

	public List<ObjectiveDecompositionRule> rulesFor(ObjectiveType type) {
		return map.getOrDefault(type, emptyList());
	}

	private static Map<ObjectiveType, List<ObjectiveDecompositionRule>> convertRulesArrayToMap(ObjectiveDecompositionRule[] rules) {
		Map<ObjectiveType, List<ObjectiveDecompositionRule>> map = new HashMap<>();
		for (ObjectiveDecompositionRule rule : rules) {
			map.computeIfAbsent(rule.objective(), key -> new ArrayList<>()).add(rule);
		}
		return map;
	}

}
