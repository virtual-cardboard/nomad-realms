package nomadrealms.context.game.card.collection;

import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.zone.CardZone;
import nomadrealms.context.game.zone.Deck;

public class DeckList extends CardZone<GameCard> {

	public DeckList(GameCard... cards) {
		super(cards);
	}

	public Deck toDeck() {
		Deck deck = new Deck();
		for (GameCard card : cards) {
			deck.addCard(new WorldCard(card));
		}
		return deck;
	}

}