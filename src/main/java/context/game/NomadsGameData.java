package context.game;

import context.data.GameData;
import model.GameState;

public class NomadsGameData extends GameData {

	private GameState state = new GameState();

	@Override
	protected void init() {
	}

	public GameState state() {
		return state;
	}

}
