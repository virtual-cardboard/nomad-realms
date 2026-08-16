package nomadrealms.context.game.card;

import java.util.List;

/**
 * Interface for cards in the game.
 *
 * @author Lunkle
 */
public interface Card {

	/**
	 * Returns the type of the card.
	 *
	 * @return the card type
	 */
	CardType type();

	/**
	 * Returns the list of keywords associated with the card.
	 *
	 * @return list of keywords
	 */
	List<CardKeyword> keywords();

}
