package nomadrealms.context.game.card;

import java.util.List;

/**
 * This object wraps an in-game card for storage in collections.
 *
 * @author Lunkle
 */
public class CollectionCard implements Card {

	GameCard card;

	public CollectionCard(GameCard card) {
		this.card = card;
	}

	@Override
	public CardType type() {
		return card.type();
	}

	@Override
	public List<CardKeyword> keywords() {
		return card.keywords();
	}

	public GameCard card() {
		return card;
	}

}
