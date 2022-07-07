package model.id;

import model.actor.ItemActor;
import model.state.GameState;

public class ItemId extends ActorId {

	public ItemId(long id) {
		super(id);
	}

	@Override
	public ItemActor getFrom(GameState state) {
		return state.item(id);
	}

}
