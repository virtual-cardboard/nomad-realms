package model.actor;

import model.GameState;

public abstract class CardPlayer extends PositionalActor {

	@Override
	public void addTo(GameState state) {
		super.addTo(state);
		state.addCardPlayer(this);
	}

}
