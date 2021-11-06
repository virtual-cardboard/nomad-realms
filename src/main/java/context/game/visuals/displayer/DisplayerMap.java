package context.game.visuals.displayer;

import java.util.HashMap;
import java.util.Map;

import model.actor.Actor;
import model.actor.Nomad;

public class DisplayerMap {

	private Map<Actor, ActorDisplayer<?>> displayers = new HashMap<>();

	public <T extends Actor> void put(T actor, ActorDisplayer<T> displayer) {
		displayers.put(actor, displayer);
	}

	@SuppressWarnings("unchecked")
	public <T extends Actor> ActorDisplayer<T> get(T actor) {
		return (ActorDisplayer<T>) displayers.get(actor);
	}

	public NomadDisplayer getNomadDisplayer(Nomad nomad) {
		return (NomadDisplayer) displayers.get(nomad);
	}

}
