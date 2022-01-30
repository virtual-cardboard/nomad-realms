package model.hidden;

import java.util.List;

public abstract class GameObjective {

	public List<GameObjectiveType> subObjectives;

	public abstract GameObjective copy();

}
