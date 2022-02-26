package model.card.chain;

import java.util.Queue;

import common.event.GameEvent;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.id.ID;
import model.id.TileID;
import model.state.GameState;

public class TeleportEvent extends FixedTimeChainEvent {

	private TileID tileID;

	public TeleportEvent(ID<? extends CardPlayer> playerID, TileID tileID) {
		super(playerID);
		this.tileID = tileID;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
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
