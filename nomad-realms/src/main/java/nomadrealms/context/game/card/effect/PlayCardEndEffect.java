package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;
import engine.serialization.Derializable;

@Derializable
public class PlayCardEndEffect extends Effect {

	private final WorldCard card;

	public PlayCardEndEffect(CardPlayer source, WorldCard card) {
		super(source);
		this.card = card;
	}

	@Override
	public void resolve(World world) {

	}

	@Override
	public CardPlayer source() {
		return (CardPlayer) super.source();
	}

	public WorldCard card() {
		return card;
	}

	/**
	 * No-arg constructor for serialization.
	 */
	PlayCardEndEffect() {
		this.card = null;
	}
}

