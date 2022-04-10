package model.chain.event;

import java.util.List;

import engine.common.QueueGroup;
import engine.common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.ItemActor;
import model.id.CardPlayerID;
import model.state.GameState;

public class GatherItemsEvent extends FixedTimeChainEvent {

	private int radius;

	public GatherItemsEvent(CardPlayerID playerID, int radius) {
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
		return 4;
	}

	@Override
	public void process(long tick, GameState state, QueueGroup queueGroup) {
		CardPlayer player = playerID().getFrom(state);
		Vector2i chunkPos = player.worldPos().chunkPos();
		List<Actor> actorsAroundChunk = state.getActorsAroundChunk(chunkPos);
		for (Actor actor : actorsAroundChunk) {
			if (actor instanceof ItemActor && actor.worldPos().distanceTo(player.worldPos()) <= radius) {
				ItemActor itemActor = (ItemActor) actor;
				player.inventory().add(itemActor.item(), 1);
				itemActor.setShouldRemove(true);
			}
		}
	}

	@Override
	public String textureName() {
		return "gather_items";
	}

}
