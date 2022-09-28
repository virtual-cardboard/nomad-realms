package model.hidden.objective.decomposition;

import model.actor.health.cardplayer.NpcActor;
import model.hidden.objective.ObjectiveCriteria;
import model.hidden.objective.ObjectiveSupplier;
import model.hidden.objective.ObjectiveType;
import model.state.GameState;

public class ObjectiveDecompositionRule {

	/**
	 * The objective type that this rule applies to.
	 */
	private ObjectiveType objective;
	/**
	 * If the criteria is null, then the rule is always applicable.
	 */
	private ObjectiveCriteria criteria;
	/**
	 * The decomposition is an array of objective suppliers ordered left to right.
	 */
	private ObjectiveSupplier[] decomposition;

	public ObjectiveDecompositionRule(ObjectiveType objective, ObjectiveCriteria criteria, ObjectiveSupplier... decomposition) {
		this.objective = objective;
		this.criteria = criteria;
		this.decomposition = decomposition;
	}

	public ObjectiveType objective() {
		return objective;
	}

	public ObjectiveCriteria criteria() {
		return criteria;
	}

	public ObjectiveSupplier[] decomposition() {
		return decomposition;
	}

	public boolean isApplicableFor(NpcActor npc, GameState state) {
		if (criteria == null) {
			return true;
		}
		return criteria.test(npc, state);
	}

}
