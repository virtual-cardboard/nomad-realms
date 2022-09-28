package model.hidden.objective;

import java.util.List;

import model.actor.health.cardplayer.NpcActor;
import model.hidden.objective.decomposition.ObjectiveDecompositionRule;
import model.hidden.objective.decomposition.ObjectiveDecompositionRulebook;
import model.state.GameState;

public class ObjectiveTree {

	//	private Objective root;
	private Objective objective;

	public ObjectiveTree(Objective objective) {
//		root = objective;
		this.objective = objective;
	}

	public void applyRulebook(ObjectiveDecompositionRulebook rulebook, NpcActor actor, GameState state) {
		List<ObjectiveDecompositionRule> rules = rulebook.rulesFor(objective.type());
		for (int i = 0, m = rules.size(); i < m; i++) {
			ObjectiveDecompositionRule rule = rules.get(i);
			if (rule.isApplicableFor(actor, state)) {
				decomposeObjective(rule, actor, state);
			}
		}
	}

	/**
	 * Helper method to decompose the current objective into sub-objectives and descend to the first sub-objective.
	 *
	 * @param subObjectives
	 * @param actor
	 * @param state
	 */
	private void decomposeObjective(ObjectiveDecompositionRule rule, NpcActor actor, GameState state) {
		ObjectiveSupplier[] subObjectives = rule.decomposition();
		for (int i = 0, m = subObjectives.length; i < m; i++) {
			ObjectiveSupplier subObjective = subObjectives[i];
			objective.addSubObjective(subObjective.generate(objective, actor, state));
		}
		try {
			objective = objective.subObjectives().get(0);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("Expected decomposition of rule " + rule + " to have at least one sub-objective. Please add at least one sub-objective.");
		}
	}

}
