package nomadrealms.context.game.indexing;

import nomadrealms.context.game.actor.Actor;
import java.util.Collection;

public abstract class ActorLookup {

	public abstract void register(Actor actor);

	public abstract void unregister(Actor actor);

	public abstract Actor get(Lookup lookup);

	public abstract Collection<Actor> all();

}
