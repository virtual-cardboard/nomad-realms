package model.chain.event;

import engine.common.QueueGroup;
import math.IdGenerators;
import model.actor.Actor;
import model.id.CardPlayerId;
import model.id.TileId;
import model.state.GameState;

public class TeleportEvent extends FixedTimeChainEvent {

	private TileId tileID;

	public TeleportEvent(CardPlayerId playerID, TileId tileID) {
		super(playerID);
		this.tileID = tileID;
	}

	@Override
	public void process(long tick, GameState state, IdGenerators idGenerators, QueueGroup queueGroup) {
		Actor actor = playerID().getFrom(state);
		actor.worldPos().set(tileID.getFrom(state).worldPos());
	}

	@Override
	public int priority() {
		return 9;
	}

	@Override
	public int processTime() {
		return 1;
	}

	@Override
	public String textureName() {
		return "teleport";
	}

}
