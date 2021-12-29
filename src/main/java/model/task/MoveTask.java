package model.task;

import static common.math.Vector2f.ORIGIN;
import static model.tile.Tile.TILE_HEIGHT;

import common.math.Vector2f;
import model.GameState;
import model.actor.CardPlayer;
import model.actor.GameObject;
import model.tile.Tile;

public class MoveTask extends Task {

	private boolean done;

	@Override
	public void begin(CardPlayer cardPlayer, GameObject target, GameState state) {
		resume(cardPlayer, target, state);
	}

	@Override
	public void execute(CardPlayer cardPlayer, GameObject target, GameState state) {
		Tile tile = (Tile) target;
		Vector2f relativePos = cardPlayer.relativePos(tile.chunk().pos(), tile.pos());
		if (relativePos.lengthSquared() <= 200 * 200) {
			cardPlayer.setChunkPos(tile.chunk().pos());
			cardPlayer.updatePos(tile.pos());
			cardPlayer.setDirection(new Vector2f(0, 1));
			cardPlayer.setVelocity(ORIGIN);
			done = true;
		}
	}

	@Override
	public void pause(CardPlayer cardPlayer, GameObject target, GameState state) {
		cardPlayer.setDirection(new Vector2f(0, 1));
		cardPlayer.setVelocity(ORIGIN);
	}

	@Override
	public void resume(CardPlayer cardPlayer, GameObject target, GameState state) {
		Tile tile = (Tile) target;
		Vector2f relativePos = cardPlayer.relativePos(tile.chunk().pos(), tile.pos());
		Vector2f dir = relativePos.negate().normalise();
		cardPlayer.setDirection(dir);
		cardPlayer.setVelocity(relativePos.negate().normalise().scale(TILE_HEIGHT / 10 * 2));
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
