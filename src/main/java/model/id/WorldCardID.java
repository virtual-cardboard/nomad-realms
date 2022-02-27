package model.id;

import model.card.WorldCard;
import model.state.GameState;

public class WorldCardID extends ID {

	public WorldCardID(long id) {
		super(id);
	}

	@Override
	public WorldCard getFrom(GameState state) {
		return state.card(id);
	}

}
