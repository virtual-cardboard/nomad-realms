package nomadrealms.event.game.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class EffectContext {

	private World world;
	private Target target;
	private Actor source;
	private WorldCard card;

	public EffectContext world(World world) {
		this.world = world;
		return this;
	}

	public World world() {
		return world;
	}

	public EffectContext target(Target target) {
		this.target = target;
		return this;
	}

	public Target target() {
		return target;
	}

	public EffectContext source(Actor source) {
		this.source = source;
		return this;
	}

	public Actor source() {
		return source;
	}

	public EffectContext card(WorldCard card) {
		this.card = card;
		return this;
	}

	public WorldCard card() {
		return card;
	}

}
