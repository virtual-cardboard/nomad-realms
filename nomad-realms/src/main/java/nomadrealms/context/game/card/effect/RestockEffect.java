package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.zone.Deck;

public class RestockEffect extends Effect {

	private Deck deck;

	public RestockEffect(CardPlayer source, Deck deck) {
		super(source);
		this.deck = deck;
		this.delay(15);
	}

	@Override
	public void resolve(World world) {
		deck.restock(((CardPlayer) source()).deckCollection().discardZone(), source().tile().chunk().zone().rng());
	}

}
