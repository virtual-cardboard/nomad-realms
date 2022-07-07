package model.id;

import model.card.WorldCard;
import model.state.GameState;

public class WorldCardId extends Id {

	public WorldCardId(long id) {
		super(id);
	}

	@Override
	public WorldCard getFrom(GameState state) {
		return state.card(id);
	}

}
