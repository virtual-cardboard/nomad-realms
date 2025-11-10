package nomadrealms.context.game.card;

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

	public GameCard card() {
		return card;
	}

}
