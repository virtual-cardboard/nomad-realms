package model.task;

import engine.common.math.Vector2i;
import math.WorldPos;
import model.actor.CardPlayer;
import model.id.CardPlayerID;
import model.id.TileID;
import model.state.GameState;

public class MoveTask extends Task {

	private int timer = 10;
	private boolean done;

	public MoveTask() {
	}

	public MoveTask(long id) {
		super(id);
	}

	@Override
	public void execute(CardPlayerID playerID, GameState state) {
		if (timer != 0) {
			timer--;
			return;
		}
		timer = 0;
		CardPlayer player = playerID.getFrom(state);
		WorldPos playerPos = player.worldPos();
		WorldPos targetPos = ((TileID) targetID()).getFrom(state).worldPos();
		if (playerPos.equals(targetPos)) {
			done = true;
		}
		Vector2i tilePos = playerPos.tilePos();
		playerPos.setTilePos(tilePos.add(playerPos.directionTo(targetPos)));
	}

	@Override
	public void pause(CardPlayerID playerID, GameState state) {
		timer = 10;
	}

	@Override
	public void resume(CardPlayerID playerID, GameState state) {
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public MoveTask copy() {
		MoveTask copy = new MoveTask(id);
		copy.timer = timer;
		copy.done = done;
		return super.copyTo(copy);
	}

	@Override
	public String name() {
		return "move";
	}

}
