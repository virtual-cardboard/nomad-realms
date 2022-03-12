package util;

import model.hidden.objective.ObjectiveType;
import model.hidden.objective.decomposition.util.ObjectiveDecompositionRoot;

public class UtilDecompose {

	public static ObjectiveDecompositionRoot decompose(ObjectiveType root) {
		return new ObjectiveDecompositionRoot(root);
	}

}
