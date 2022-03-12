package model.hidden.objective.decomposition.util;

import model.hidden.objective.ObjectiveCriteria;
import model.hidden.objective.ObjectiveSupplier;
import model.hidden.objective.ObjectiveType;

public class ObjectiveDecompositionRoot {
	private ObjectiveType objective;

	public ObjectiveDecompositionRoot(ObjectiveType objective) {
		this.objective = objective;
	}

	public ObjectiveDecompositionRootAndDecomposition into(ObjectiveSupplier... decomposition) {
		return new ObjectiveDecompositionRootAndDecomposition(objective, decomposition);
	}

	public ObjectiveDecompositionRootWithCriteria always() {
		return new ObjectiveDecompositionRootWithCriteria(objective, null);
	}

	public ObjectiveDecompositionRootWithCriteria when(ObjectiveCriteria criteria) {
		return new ObjectiveDecompositionRootWithCriteria(objective, criteria);
	}
}
