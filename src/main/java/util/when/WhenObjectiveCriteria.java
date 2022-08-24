package util.when;

import model.hidden.objective.ObjectiveCriteria;
import model.hidden.objective.ObjectiveType;
import model.hidden.objective.decomposition.util.ObjectiveDecompositionRootWithCriteria;

public class WhenObjectiveCriteria {

	private final ObjectiveCriteria criteria;

	public WhenObjectiveCriteria(ObjectiveCriteria criteria) {
		this.criteria = criteria;
	}

	public ObjectiveDecompositionRootWithCriteria thenDecompose(ObjectiveType root) {
		return new ObjectiveDecompositionRootWithCriteria(root, criteria);
	}

}
