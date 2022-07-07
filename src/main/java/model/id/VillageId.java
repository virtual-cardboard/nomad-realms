package model.id;

import model.hidden.village.Village;
import model.state.GameState;

public class VillageId extends HiddenGameObjectId {

	public VillageId(long id) {
		super(id);
	}

	@Override
	public Village getFrom(GameState state) {
		return (Village) state.hiddens().get(id);
	}

}
