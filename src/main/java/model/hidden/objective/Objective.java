package model.hidden.objective;

import java.util.ArrayList;
import java.util.List;

import model.actor.NPCActor;
import model.state.GameState;

public class Objective {

	private final ObjectiveType type;
	private List<Objective> subObjectives = new ArrayList<>();
	private Objective parent;
	private ObjectiveCriteria completionCriteria;

	public Objective(ObjectiveType type, Objective parent, ObjectiveCriteria completionCriteria) {
		this.type = type;
		this.parent = parent;
		this.completionCriteria = completionCriteria;
	}

	public ObjectiveType type() {
		return type;
	}

	public List<Objective> subObjectives() {
		return subObjectives;
	}

	public boolean isComplete(NPCActor npc, GameState state) {
		return completionCriteria.test(npc, state);
	}

	public Objective addSubObjective(ObjectiveType type) {
		Objective objective = new Objective(type, this, null);
		subObjectives.add(objective);
		return this;
	}

	public Objective addSubObjective(Objective objective) {
		subObjectives.add(objective);
		return this;
	}

	public Objective addSubObjective(ObjectiveType type, ObjectiveCriteria completionCriteria) {
		Objective objective = new Objective(type, this, completionCriteria);
		subObjectives.add(objective);
		return this;
	}

	public Objective parent() {
		return parent;
	}

	public void removeFirstSubObjective() {
		subObjectives.remove(0);
	}

	@Override
	public String toString() {
		return type.name();
	}

}
