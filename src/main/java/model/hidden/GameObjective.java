package model.hidden;

import java.util.Arrays;
import java.util.List;

public abstract class GameObjective {

	public GameObjective(GameObjectiveType... subObjectives) {
		this.subObjectives = Arrays.asList(subObjectives);
	}

	public List<GameObjectiveType> subObjectives;

	public abstract GameObjective copy();

}
