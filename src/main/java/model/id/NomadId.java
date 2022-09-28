package model.id;

import model.actor.health.cardplayer.Nomad;
import model.state.GameState;

public class NomadId extends CardPlayerId {

	public NomadId(long id) {
		super(id);
	}

	@Override
	public Nomad getFrom(GameState state) {
		return (Nomad) state.actor(id);
	}

}
