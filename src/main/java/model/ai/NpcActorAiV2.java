package model.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import event.logicprocessing.CardPlayedEvent;
import model.actor.NpcActor;
import model.hidden.objective.Objective;
import model.hidden.objective.ObjectiveTree;
import model.hidden.objective.ObjectiveType;
import model.hidden.objective.decomposition.ObjectiveDecompositionRule;
import model.state.GameState;

public abstract class NpcActorAiV2 {

	protected ObjectiveTree objectiveTree;
	protected Map<ObjectiveType, List<ObjectiveDecompositionRule>> decompositionRulesMap;

	public NpcActorAiV2(ObjectiveType root, ObjectiveDecompositionRule... rules) {
		objectiveTree = new ObjectiveTree(new Objective(root, null, null));
		decompositionRulesMap = convertRulesArrayToMap(rules);
	}

	public CardPlayedEvent update(NpcActor npc, long tick, GameState state) {
		return playCard(npc, state);
	}

	public abstract CardPlayedEvent playCard(NpcActor npc, GameState state);

	public abstract NpcActorAiV2 copy();

	public <A extends NpcActorAiV2> A copyTo(A ai) {
		// TODO
		ai.objectiveTree = objectiveTree;
		ai.decompositionRulesMap = decompositionRulesMap;
		return ai;
	}

	private static Map<ObjectiveType, List<ObjectiveDecompositionRule>> convertRulesArrayToMap(ObjectiveDecompositionRule[] rules) {
		Map<ObjectiveType, List<ObjectiveDecompositionRule>> map = new HashMap<>();
		for (ObjectiveDecompositionRule rule : rules) {
			map.computeIfAbsent(rule.objective(), key -> new ArrayList<>()).add(rule);
		}
		return map;
	}

}
