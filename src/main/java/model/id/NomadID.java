package model.id;

import model.actor.Nomad;
import model.state.GameState;

public class NomadID extends CardPlayerID {

	public NomadID(long id) {
		super(id);
	}

	@Override
	public Nomad getFrom(GameState state) {
		return (Nomad) state.actor(id);
	}

}
