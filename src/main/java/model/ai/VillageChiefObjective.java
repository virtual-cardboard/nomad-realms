package model.ai;

import model.hidden.GameObjective;

public class VillageChiefObjective extends GameObjective {

	public VillageChiefObjective() {
		super();
	}

	@Override
	public GameObjective copy() {
		return new VillageChiefObjective();
	}

}
