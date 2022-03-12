package util;

import model.hidden.objective.ObjectiveCriteria;
import util.when.WhenObjectiveCriteria;

public class UtilWhen {

	private UtilWhen() {
	}

	public static WhenObjectiveCriteria when(ObjectiveCriteria criteria) {
		return new WhenObjectiveCriteria(criteria);
	}

}
