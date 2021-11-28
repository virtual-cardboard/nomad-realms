package context.game;

import context.data.GameData;
import model.GameState;
import model.actor.CardPlayer;

public class NomadsGameData extends GameData {

	private CardPlayer player;
	private GameState state = new GameState();

	@Override
	protected void init() {
		player = state.cardPlayer(1L);
	}

	public CardPlayer player() {
		return player;
	}

	public void setPlayer(CardPlayer player) {
		this.player = player;
	}

	public GameState state() {
		return state;
	}

}
