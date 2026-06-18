package nomadrealms.context.game.actor.factory;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.Bub;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.cardplayer.FeralMonkey;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.actor.types.cardplayer.VillageLumberjack;
import nomadrealms.context.game.actor.types.cardplayer.Wolf;

public class ActorFactory {

	public static Actor createActor(ActorType type, String name) {
		switch (type) {
			case VILLAGE_CHIEF:
				return new VillageChief(name);
			case VILLAGE_LUMBERJACK:
				return new VillageLumberjack(name, null);
			case FERAL_MONKEY:
				return new FeralMonkey(name, null);
			case WOLF:
				return new Wolf(name, null);
			case FARMER:
				return new Farmer(name, null);
			case BUB:
				return new Bub(name, null);
			case NOMAD:
				return new Nomad(name, null);
			default:
				throw new RuntimeException("No actor case in ActorFactory for actor type " + type);
		}
	}

}
