package nomadrealms.context.game.card.effect;

import java.util.List;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.world.World;

public class SurfaceCardEffect extends Effect {

	private final List<WorldCard> cards;

	public SurfaceCardEffect(Actor source, List<WorldCard> cards) {
		super(source);
		this.cards = cards;
	}

	@Override
	public void resolve(World world) {
		for (WorldCard card : cards) {
			System.out.println("Surfacing card: " + card);
			card.zone().surface(card);
		}
	}
}
