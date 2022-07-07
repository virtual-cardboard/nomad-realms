package model.hidden.objective.decomposition;

import model.actor.NpcActor;
import model.hidden.objective.ObjectiveCriteria;
import model.hidden.objective.ObjectiveSupplier;
import model.hidden.objective.ObjectiveType;
import model.state.GameState;

public class ObjectiveDecompositionRule {

	private ObjectiveType objective;
	private ObjectiveCriteria criteria;
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
