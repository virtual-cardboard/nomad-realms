package model.hidden;

public class LeafNodeObjective extends GameObjective {

	private GameObjectiveType objectiveType;

	public LeafNodeObjective(GameObjectiveType objectiveType) {
		this.objectiveType = objectiveType;
	}

	public GameObjectiveType objectiveType() {
		return objectiveType;
	}

	@Override
	public LeafNodeObjective copy() {
		return this;
	}

}
