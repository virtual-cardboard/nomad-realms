package model.task;

import common.math.Vector2i;
import math.WorldPos;
import model.actor.CardPlayer;
import model.id.ID;
import model.state.GameState;
import model.world.Tile;

public class MoveTask extends Task {

	private int timer = 10;
	private boolean done;

	@Override
	public void execute(ID<? extends CardPlayer> playerID, GameState state) {
		if (timer != 0) {
			timer--;
			return;
		}
		timer = 10;
		CardPlayer player = playerID.getFrom(state);
		WorldPos playerPos = player.worldPos();
		WorldPos targetPos = state.worldMap().finalLayerChunk(targetID()).tile(Tile.tileCoords(targetID())).worldPos();
		if (playerPos.equals(targetPos)) {
			done = true;
		}
		Vector2i tilePos = playerPos.tilePos();
		playerPos.setTilePos(tilePos.add(playerPos.directionTo(targetPos)));
	}

	@Override
	public void pause(ID<? extends CardPlayer> playerID, GameState state) {
		timer = 10;
	}

	@Override
	public void resume(ID<? extends CardPlayer> playerID, GameState state) {
	}

	@Override
	public boolean isDone() {
		if (done) {
			System.out.println("Done");
		}
		return done;
	}

	@Override
	public MoveTask copy() {
		MoveTask copy = new MoveTask();
		copy.timer = timer;
		copy.done = done;
		return super.copyTo(copy);
	}

	@Override
	public String name() {
		return "move";
	}

}
