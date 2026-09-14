package nomadrealms.context.game.card;

/**
 * The type of a card.
 *
 * @author Lunkle
 */
import engine.serialization.Derializable;

@Derializable
public enum CardType {
	ACTION,
	STRUCTURE,
	CREATURE
}
