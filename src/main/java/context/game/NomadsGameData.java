package context.game;

import static math.IDGenerator.genID;

import context.data.GameData;
import model.GameState;

public class NomadsGameData extends GameData {

	private long playerID = genID();
	private GameState state = new GameState();

	@Override
	protected void init() {
	}

	public long playerID() {
		return playerID;
	}

	public GameState state() {
		return state;
	}

}
