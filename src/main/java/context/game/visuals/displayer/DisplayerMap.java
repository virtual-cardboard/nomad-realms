package context.game.visuals.displayer;

import java.util.HashMap;
import java.util.Map;

import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.Nomad;

public class DisplayerMap {

	private Map<Actor, CardPlayerDisplayer<?>> displayers = new HashMap<>();

	public <T extends CardPlayer> void put(T actor, CardPlayerDisplayer<T> displayer) {
		displayers.put(actor, displayer);
	}

	@SuppressWarnings("unchecked")
	public <T extends CardPlayer> CardPlayerDisplayer<T> get(T actor) {
		return (CardPlayerDisplayer<T>) displayers.get(actor);
	}

	public NomadDisplayer getNomadDisplayer(Nomad nomad) {
		return (NomadDisplayer) displayers.get(nomad);
	}

}
