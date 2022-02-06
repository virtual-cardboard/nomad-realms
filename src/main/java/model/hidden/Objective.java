package model.hidden;

import java.util.ArrayList;
import java.util.List;

public class Objective {

	private ObjectiveType type;
	private List<Objective> subObjectives;
	private Objective parent;

	public Objective(ObjectiveType type) {
		this.type = type;
	}

	public Objective(ObjectiveType type, Objective parent) {
		this.type = type;
		this.parent = parent;
	}

	public ObjectiveType type() {
		return type;
	}

	public List<Objective> subObjectives() {
		return subObjectives;
	}

	public void setSubObjectives(ObjectiveType... subObjectiveTypes) {
		List<Objective> subObjectives = new ArrayList<>();
		for (ObjectiveType type : subObjectiveTypes) {
			Objective objective = new Objective(type, this);
			subObjectives.add(objective);
		}
		this.subObjectives = subObjectives;
	}

	public Objective parent() {
		return parent;
	}

}
