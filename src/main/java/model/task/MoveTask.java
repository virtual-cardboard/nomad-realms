package model.task;

import common.math.Vector2i;
import model.actor.CardPlayer;
import model.state.GameState;
import model.world.Tile;

public class MoveTask extends Task {

	private int timer = 10;
	private boolean done;

	@Override
	public void execute(long playerID, GameState state) {
		if (timer != 0) {
			timer--;
			return;
		}
		timer = 10;
		CardPlayer player = state.cardPlayer(playerID);
		Tile tile = state.worldMap().finalLayerChunk(targetID()).tile(Tile.tileCoords(targetID()));
		Vector2i tilePos = player.worldPos().tilePos();
//		player.worldPos().setTilePos(tilePos.add(tile.worldPos().sub ));
		if (player.worldPos().equals(tile.worldPos())) {
			done = true;
		}
	}

	@Override
	public void pause(long playerID, GameState state) {
		CardPlayer player = state.cardPlayer(playerID);
//		player.setDirection(new Vector2f(0, 1));
//		player.setVelocity(ORIGIN);
	}

	@Override
	public void resume(long playerID, GameState state) {
		CardPlayer player = state.cardPlayer(playerID);
		Tile tile = state.worldMap().finalLayerChunk(targetID()).tile(Tile.tileCoords(targetID()));
//		Vector2f relativePos = player.relativePos(tile.chunk().pos(), tile.pos());
//		Vector2f dir = relativePos.negate().normalise();
//		player.setDirection(dir);
//		player.setVelocity(relativePos.negate().normalise().scale(TILE_HEIGHT / 10 * 2));
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public MoveTask copy() {
		MoveTask copy = new MoveTask();
		copy.timer = timer;
		return copy;
	}

}
