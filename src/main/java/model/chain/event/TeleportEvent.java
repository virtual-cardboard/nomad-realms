package model.chain.event;

import common.QueueGroup;
import model.actor.Actor;
import model.id.CardPlayerID;
import model.id.TileID;
import model.state.GameState;

public class TeleportEvent extends FixedTimeChainEvent {

	private TileID tileID;

	public TeleportEvent(CardPlayerID playerID, TileID tileID) {
		super(playerID);
		this.tileID = tileID;
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
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
