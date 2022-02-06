package model.hidden;

public enum ObjectiveType {

	VILLAGER_SURVIVE,
	BUILD_HOUSE,
	GATHER_WOOD,
	FIND_BUILD_HOUSE_LOCATION,
	ACTUALLY_BUILD_HOUSE,
	FIND_TREE,
	CUT_TREE,
	GATHER_LOG;

	public Objective createObjective() {
		return new Objective(this);
	}

}
