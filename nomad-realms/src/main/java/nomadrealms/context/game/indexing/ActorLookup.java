package nomadrealms.context.game.indexing;

import nomadrealms.context.game.actor.Actor;

public abstract class ActorLookup {

	public abstract void register(Actor actor);

	public abstract Actor get(Lookup lookup);

}
