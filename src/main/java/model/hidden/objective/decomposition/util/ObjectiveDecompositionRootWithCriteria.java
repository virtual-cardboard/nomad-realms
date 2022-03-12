package model.hidden.objective.decomposition.util;

import model.hidden.objective.ObjectiveCriteria;
import model.hidden.objective.ObjectiveSupplier;
import model.hidden.objective.ObjectiveType;
import model.hidden.objective.decomposition.ObjectiveDecompositionRule;

public class ObjectiveDecompositionRootWithCriteria {
	private ObjectiveType objective;
	private ObjectiveCriteria criteria;

	public ObjectiveDecompositionRootWithCriteria(ObjectiveType root, ObjectiveCriteria criteria) {
		this.criteria = criteria;
	}

	public ObjectiveDecompositionRule into(ObjectiveSupplier... decomposition) {
		return new ObjectiveDecompositionRule(objective, criteria, decomposition);
	}
}