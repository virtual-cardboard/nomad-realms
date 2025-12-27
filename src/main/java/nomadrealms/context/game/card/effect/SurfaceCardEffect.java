package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.card.WorldCard;

public class SurfaceCardEffect extends Effect {

	private final List<WorldCard> cards;

	public SurfaceCardEffect(List<WorldCard> cards) {
		this.cards = cards;
	}

	@Override
	public void resolve() {
		for (WorldCard card : cards) {
			card.zone().surface(card);
		}
	}
}
