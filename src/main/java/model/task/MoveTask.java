package model.task;

import static common.math.Vector2f.ORIGIN;
import static model.world.Tile.TILE_HEIGHT;

import common.math.Vector2f;
import model.actor.CardPlayer;
import model.state.GameState;
import model.world.Tile;

public class MoveTask extends Task {

	private boolean done;

	@Override
	public void execute(long playerID, GameState state) {
		CardPlayer player = state.cardPlayer(playerID);
		Tile tile = state.worldMap().finalLayerChunk(targetID()).tile(Tile.tileCoords(targetID()));
		Vector2f relativePos = player.relativePos(tile.chunk().pos(), tile.pos());
		if (relativePos.lengthSquared() <= 200 * 200) {
			player.setChunkPos(tile.chunk().pos());
			player.updatePos(tile.pos());
			player.setDirection(new Vector2f(0, 1));
			player.setVelocity(ORIGIN);
			done = true;
		}
	}

	@Override
	public void pause(long playerID, GameState state) {
		CardPlayer player = state.cardPlayer(playerID);
		player.setDirection(new Vector2f(0, 1));
		player.setVelocity(ORIGIN);
	}

	@Override
	public void resume(long playerID, GameState state) {
		CardPlayer player = state.cardPlayer(playerID);
		Tile tile = state.worldMap().finalLayerChunk(targetID()).tile(Tile.tileCoords(targetID()));
		Vector2f relativePos = player.relativePos(tile.chunk().pos(), tile.pos());
		Vector2f dir = relativePos.negate().normalise();
		player.setDirection(dir);
		player.setVelocity(relativePos.negate().normalise().scale(TILE_HEIGHT / 10 * 2));
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
