package model.task;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.SerializationFormatEnum;
import engine.common.math.Vector2i;
import math.WorldPos;
import model.actor.CardPlayer;
import model.id.CardPlayerId;
import model.id.TileId;
import model.state.GameState;

public class MoveTask extends Task {

	private int timer = 10;

	public MoveTask() {
	}

	@Override
	public void execute(CardPlayerId playerID, GameState state) {
		if (timer != 0) {
			timer--;
			return;
		}
		timer = 4;
		CardPlayer player = playerID.getFrom(state);
		WorldPos playerPos = player.worldPos();
		WorldPos targetPos = ((TileId) targetID()).getFrom(state).worldPos();
		if (playerPos.equals(targetPos)) {
			setDone(true);
		}
		Vector2i tilePos = playerPos.tilePos();
		playerPos.setTilePos(tilePos.add(playerPos.directionTo(targetPos)));
	}

	@Override
	public void pause(CardPlayerId playerID, GameState state) {
		timer = 10;
	}

	@Override
	public void resume(CardPlayerId playerID, GameState state) {
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

	@Override
	public void read(SerializationReader reader) {

	}

	@Override
	public void write(SerializationWriter writer) {

	}

	@Override
	public SerializationFormatEnum formatEnum() {
		return null;
	}

}
