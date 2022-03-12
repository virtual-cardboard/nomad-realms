package model.hidden.objective.decomposition.util;

import model.hidden.objective.ObjectiveCriteria;
import model.hidden.objective.ObjectiveSupplier;
import model.hidden.objective.ObjectiveType;
import model.hidden.objective.decomposition.ObjectiveDecompositionRule;

public class ObjectiveDecompositionRootAndDecomposition {

	private ObjectiveType objective;
	private ObjectiveSupplier[] decomposition;

	public ObjectiveDecompositionRootAndDecomposition(ObjectiveType objective, ObjectiveSupplier... decomposition) {
		this.objective = objective;
		this.decomposition = decomposition;
	}

	public ObjectiveDecompositionRule always() {
		return new ObjectiveDecompositionRule(objective, null, decomposition);
	}

	public ObjectiveDecompositionRule when(ObjectiveCriteria criteria) {
		return new ObjectiveDecompositionRule(objective, criteria, decomposition);
	}
}