package nomadrealms.context.game.indexing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import nomadrealms.context.game.actor.Actor;

public class HashActorLookup extends ActorLookup {

	private final Map<UUID, Actor> actors = new HashMap<>();

	@Override
	public void register(Actor actor) {
		actors.put(actor.uuid(), actor);
	}

	@Override
	public void unregister(Actor actor) {
		actors.remove(actor.uuid());
	}


	@Override
	public Actor get(Lookup lookup) {
		return actors.get(lookup.uuid());
	}

	@Override
	public Collection<Actor> all() {
		return actors.values();
	}

}
