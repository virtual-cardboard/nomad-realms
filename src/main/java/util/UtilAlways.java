package util;

import model.hidden.objective.ObjectiveType;
import model.hidden.objective.decomposition.util.ObjectiveDecompositionRootWithCriteria;

public class UtilAlways {

	private UtilAlways() {
	}

	public static UtilAlways always() {
		return new UtilAlways();
	}

	public ObjectiveDecompositionRootWithCriteria decompose(ObjectiveType root) {
		return new ObjectiveDecompositionRootWithCriteria(root, null);
	}

}
