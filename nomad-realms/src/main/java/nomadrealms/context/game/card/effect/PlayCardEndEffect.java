package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;

public class PlayCardEndEffect extends Effect {

	private final WorldCard card;

	public PlayCardEndEffect(CardPlayer source, WorldCard card) {
		this.source = source;
		this.card = card;
	}

	@Override
	public void resolve(World world) {

	}

	public CardPlayer source() {
		return (CardPlayer) source;
	}

	public WorldCard card() {
		return card;
	}

}
