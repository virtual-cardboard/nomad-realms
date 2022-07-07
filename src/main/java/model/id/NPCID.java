package model.id;

import model.actor.NpcActor;
import model.state.GameState;

public class NPCID extends CardPlayerId {

	public NPCID(long id) {
		super(id);
	}

	@Override
	public NpcActor getFrom(GameState state) {
		return (NpcActor) state.actor(id);
	}

}
