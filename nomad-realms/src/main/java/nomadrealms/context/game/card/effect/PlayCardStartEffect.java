package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;
import engine.serialization.Derializable;

@Derializable
public class PlayCardStartEffect extends Effect {

	private final WorldCard card;
	private final List<Effect> effects;

	public PlayCardStartEffect(Actor source, WorldCard card, List<Effect> effects) {
		super(source);
		this.card = card;
		this.effects = effects;
	}

	@Override
	public void resolve(World world) {

	}

	public WorldCard card() {
		return card;
	}

	public List<Effect> effects() {
		return effects;
	}

	/**
	 * No-arg constructor for serialization.
	 */
	PlayCardStartEffect() {
		this.card = null;
		this.effects = null;
	}
}

