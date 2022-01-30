package model.card.chain;

import java.util.List;
import java.util.Queue;

import common.event.GameEvent;
import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.ItemActor;
import model.state.GameState;

public class GatherItemEvent extends FixedTimeChainEvent {

	private int radius;

	public GatherItemEvent(long playerID, int radius) {
		super(playerID);
		this.radius = radius;
	}

	public int radius() {
		return radius;
	}

	@Override
	public int priority() {
		return 1;
	}

	@Override
	public int processTime() {
		return 2;
	}

	@Override
	public void process(GameState state, Queue<GameEvent> sync) {
		CardPlayer player = state.cardPlayer(playerID());
		Vector2i chunkPos = player.worldPos().chunkPos();
		List<Actor> actorsAroundChunk = state.getActorsAroundChunk(chunkPos);
		for (Actor actor : actorsAroundChunk) {
			if (actor instanceof ItemActor) {
				ItemActor itemActor = (ItemActor) actor;
				player.inventory().add(itemActor.item(), 1);
				itemActor.setShouldRemove(true);
			}
		}
	}

}
