package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;

public class SurfaceCardEffect extends Effect {

	private final List<WorldCard> cards;

	public SurfaceCardEffect(List<WorldCard> cards, CardPlayer source) {
		this.cards = cards;
		source(source);
	}

	@Override
	public void resolve(World world) {
		for (WorldCard card : cards) {
			System.out.println("Surfacing card: " + card);
			card.zone().surface(card);
		}
	}
}
